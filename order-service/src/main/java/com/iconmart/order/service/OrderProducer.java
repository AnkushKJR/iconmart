package com.iconmart.order.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.iconmart.data.dto.OrderConfirmedEvent;
import com.iconmart.data.dto.OrderCreatedEvent;
import com.iconmart.data.dto.OrderRejectedEvent;

@Service
public class OrderProducer {
	
	private KafkaTemplate<Long, Object> kafkaTemplate; 

	
	public OrderProducer(KafkaTemplate<Long, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	
	public void publishOrderCreatedEvent(OrderCreatedEvent orderCreatedE) {
		kafkaTemplate.send("ORDER_CREATED", orderCreatedE.getOrderId(), orderCreatedE);
	}
	
	public void publishOrderConfirmedEvent(OrderConfirmedEvent orderConfirmedE) {
		kafkaTemplate.send("ORDER_CONFIRMED", orderConfirmedE.getOrderId(), orderConfirmedE);
	}
	
	public void publishOrderRejectedEvent(OrderRejectedEvent orderRejectedE) {
		kafkaTemplate.send("ORDER_REJECTED", orderRejectedE.getOrderId(), orderRejectedE);
	}
	
}
