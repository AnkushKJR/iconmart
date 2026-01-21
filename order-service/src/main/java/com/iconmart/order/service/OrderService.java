package com.iconmart.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iconmart.data.dto.CartProductResponseDTO;
import com.iconmart.data.dto.OrderConfirmedEvent;
import com.iconmart.data.dto.OrderCreatedEvent;
import com.iconmart.data.dto.OrderDetails;
import com.iconmart.data.dto.OrderItemEvent;
import com.iconmart.data.dto.OrderRejectedEvent;
import com.iconmart.data.dto.OrderRejectedItem;
import com.iconmart.data.dto.PaymentFailedEvent;
import com.iconmart.data.dto.PaymentSuccessEvent;
import com.iconmart.data.dto.ProductDTO;
import com.iconmart.data.dto.ProductStockReduceRequest;
import com.iconmart.order.entity.Order;
import com.iconmart.order.entity.OrderItem;
import com.iconmart.order.enums.OrderStatus;
import com.iconmart.order.feign.CartClient;
import com.iconmart.order.feign.InventoryClient;
import com.iconmart.order.repository.OrderItemRepository;
import com.iconmart.order.repository.OrderRepository;

@Service
public class OrderService {

	private final CartClient cartClient;
	private final InventoryClient inventoryClient;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final OrderProducer orderProducer;

	public OrderService(CartClient cartClient, InventoryClient inventoryClient, OrderRepository orderRepository,
			OrderItemRepository orderItemRepository, OrderProducer orderProducer) {
		this.cartClient = cartClient;
		this.inventoryClient = inventoryClient;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.orderProducer = orderProducer;
	}

	@Transactional
	public Long generateOrder(long userId) {

		// 1. Fetch cart snapshot
		// 2. Create Order (status = CREATED)
		// 3. Create OrderItems
		// 4. Save Order (transactional)
		// 5. Reduce inventory (sync call)
		// 6. Publish ORDER_CREATED event

		// 1. Fetch cart snapshot
		CartProductResponseDTO cartProductResponse = cartClient.getCartProductsForOrder(userId).getBody();

		if (cartProductResponse.getProductList().isEmpty()) {
			throw new RuntimeException("Cannot place order with empty cart");
		}

		// 2. Create Order
		Order order = new Order();
		order.setUserId(userId);
		order.setTotalPrice(cartProductResponse.getTotalPrice());
		order.setStatus(OrderStatus.CREATED);

		// 3. Create OrderItems
		List<OrderItem> items = new ArrayList<>();

		for (ProductDTO p : cartProductResponse.getProductList()) {

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProductId(p.getProductId());
			orderItem.setPrice(p.getPrice());
			orderItem.setQuantity(p.getQuantity());
			items.add(orderItem);

		}
		order.setItems(items);

		// 4. Save Order + Items [Items save because of cascading]
		Order persistedOrder = orderRepository.save(order);

		// 5. Reduce inventory
		for (OrderItem item : persistedOrder.getItems()) {
			ProductStockReduceRequest req = new ProductStockReduceRequest();
			req.setProductId(item.getProductId());
			req.setQuantity(item.getQuantity());
			inventoryClient.reduceInventory(req);
		}

		// Publish
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
		orderCreatedEvent.setOrderId(persistedOrder.getOrderId());
		orderCreatedEvent.setUserId(userId);
		orderCreatedEvent.setTotalAmount(persistedOrder.getTotalPrice());

		List<OrderItemEvent> itemsEvent = new ArrayList<>();

		for (OrderItem item : persistedOrder.getItems()) {
			OrderItemEvent orderItemEvent = new OrderItemEvent();
			orderItemEvent.setProductId(item.getProductId());
			orderItemEvent.setPrice(item.getPrice());
			orderItemEvent.setQuantity(item.getQuantity());
			itemsEvent.add(orderItemEvent);
		}

		orderCreatedEvent.setItems(itemsEvent);
		orderCreatedEvent.setCreatedAt(persistedOrder.getCreatedAt());

		orderProducer.publishOrderCreatedEvent(orderCreatedEvent);

		return persistedOrder.getOrderId();
	}

	public void orderSuccess(PaymentSuccessEvent paymentSuccessEvent) {

		Order order = orderRepository.findById(paymentSuccessEvent.getOrderId())
				.orElseThrow(() -> new RuntimeException("No order by that id"));
		
		if (order.getStatus() != OrderStatus.CREATED) {
		    return; // already processed
		}

		order.setStatus(OrderStatus.CONFIRMED);

		orderRepository.save(order);

		OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent();
		orderConfirmedEvent.setOrderId(order.getOrderId());
		orderConfirmedEvent.setUserId(order.getUserId());

		orderProducer.publishOrderConfirmedEvent(orderConfirmedEvent);

	}

	public void orderFail(PaymentFailedEvent paymentFailedEvent) {

		Order order = orderRepository.findById(paymentFailedEvent.getOrderId())
				.orElseThrow(() -> new RuntimeException("No order by that id"));
		
		if (order.getStatus() != OrderStatus.CREATED) {
		    return; // already processed
		}

		order.setStatus(OrderStatus.FAILED);

		// status changed and persisted
		Order rejectedOrder = orderRepository.save(order);

		OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent();
		orderRejectedEvent.setOrderId(rejectedOrder.getOrderId());

		List<OrderRejectedItem> rejectedItems = new ArrayList<>();

		for (OrderItem item : rejectedOrder.getItems()) {
			OrderRejectedItem orderRejectedItem = new OrderRejectedItem();
			orderRejectedItem.setProductId(item.getProductId());
			orderRejectedItem.setQuantity(item.getQuantity());
			rejectedItems.add(orderRejectedItem);
		}
		
		orderRejectedEvent.setItems(rejectedItems);

		orderProducer.publishOrderRejectedEvent(orderRejectedEvent);

	}
	
	@Transactional(readOnly = true)
	public OrderDetails fetchOrder(Long orderId) {
		
		Order order = orderRepository.findById(orderId)
										.orElseThrow(()-> new RuntimeException("No order by such id"));
		
		OrderDetails orderDetails = new OrderDetails();
		
		orderDetails.setOrderId(order.getOrderId());
		orderDetails.setUserId(order.getUserId());
		orderDetails.setTotalPrice(order.getTotalPrice());
		orderDetails.setStatus(order.getStatus());
		orderDetails.setCreatedAt(order.getCreatedAt());
		
		return orderDetails;
	}
	

}
