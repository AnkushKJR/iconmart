package com.iconmart.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iconmart.data.dto.OrderDetails;
import com.iconmart.order.service.OrderService;

@RestController
public class OrderController {

	private final OrderService orderService;
	
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@PostMapping("/orders/{userId}")
	public ResponseEntity<Long> createOrder(@PathVariable Long userId) {
		Long orderId = orderService.generateOrder(userId);
		return ResponseEntity.ok(orderId);
	}
	
	@GetMapping("/orders/{orderId}")
	public ResponseEntity<OrderDetails> getOrder(@PathVariable Long orderId) {
		OrderDetails orderDetails = orderService.fetchOrder(orderId);
		return ResponseEntity.ok(orderDetails);
	}
	
}
