package com.iconmart.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.iconmart.data.dto.OrderConfirmedEvent;
import com.iconmart.data.dto.OrderRejectedEvent;

@Service
public class NotificationConsumer {
	
	private final NotificationService notificationService;
	
	public NotificationConsumer(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	
	@KafkaListener(topics = "ORDER_CONFIRMED", groupId = "order-c-n-1")
	public void consumeOrderConfirmedEvent(OrderConfirmedEvent orderConfirmedEvent) {
		notificationService.orderConfirmedMail(orderConfirmedEvent);
	}
	
	@KafkaListener(topics = "ORDER_REJECTED", groupId = "order-r-n-1")
	public void consumeOrderRejectedEvent(OrderRejectedEvent orderRejectedEvent) {
		notificationService.orderRejectedMail(orderRejectedEvent);
	}

}
