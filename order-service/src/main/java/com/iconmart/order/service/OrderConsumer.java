package com.iconmart.order.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iconmart.data.dto.PaymentFailedEvent;
import com.iconmart.data.dto.PaymentSuccessEvent;

@Service
public class OrderConsumer {

	private final OrderService orderService;

	public OrderConsumer(OrderService orderService) {
		this.orderService = orderService;
	}
	
	//payment success: payment id, order id
	//payment failed: order id, reason
	
	@Transactional
	@KafkaListener(topics = "PAYMENT_SUCCESS", groupId = "payment-s-1")
	public void consumePaymentSuccessEvent(PaymentSuccessEvent paymentSuccessEvent) {
		orderService.orderSuccess(paymentSuccessEvent);
	}

	@Transactional
	@KafkaListener(topics = "PAYMENT_FAILED", groupId = "payment-f-1")
	public void consumePaymentFailedEvent(PaymentFailedEvent paymentFailedEvent) {
		orderService.orderFail(paymentFailedEvent);
	}

}
