package com.iconmart.payment.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.iconmart.data.dto.OrderCreatedEvent;


@Service
public class PaymentConsumer {
	
	private final PaymentService paymentService;
	
	public PaymentConsumer(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@KafkaListener(topics = "ORDER_CREATED", groupId = "order-pc-1")
	public void consumeOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
		
		paymentService.initiatePayment(orderCreatedEvent);
		
	}

}
