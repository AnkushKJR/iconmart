package com.iconmart.inventory.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iconmart.data.dto.OrderRejectedEvent;
import com.iconmart.data.dto.OrderRejectedItem;
import com.iconmart.data.dto.ProductStockIncreaseRequest;
import com.iconmart.inventory.entity.ProcessedOrderEvent;
import com.iconmart.inventory.repository.ProcessedOrderRepository;

@Service
public class InventoryConsumer {
	
	private final InventoryService inventoryService;
	private final ProcessedOrderRepository processedOrderRepository;
	
	public InventoryConsumer(InventoryService inventoryService, ProcessedOrderRepository processedOrderRepository) {
		this.inventoryService = inventoryService;
		this.processedOrderRepository = processedOrderRepository;
	}
	
	@Transactional
	@KafkaListener(topics = "ORDER_REJECTED", groupId = "order-r-1")
	public void consumeOrderRejectedEvent(OrderRejectedEvent orderRejectedE) {
		
		//idompotency check
		if(processedOrderRepository.existsByOrderId(orderRejectedE.getOrderId())) {
			return;
		}
		
		for(OrderRejectedItem rItem : orderRejectedE.getItems()) {
			ProductStockIncreaseRequest productStockIncreaseRequest = new ProductStockIncreaseRequest();
			productStockIncreaseRequest.setProductId(rItem.getProductId());
			productStockIncreaseRequest.setQuantity(rItem.getQuantity());
			inventoryService.increaseStock(productStockIncreaseRequest);
		}
		
		processedOrderRepository.save(new ProcessedOrderEvent(orderRejectedE.getOrderId()));
		
	}

}
