package com.iconmart.payment.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.iconmart.data.dto.PaymentFailedEvent;
import com.iconmart.data.dto.PaymentSuccessEvent;

@Service
public class PaymentProducer {
	
	private final KafkaTemplate<Long, Object> kafkaTemplate;
	
	public PaymentProducer(KafkaTemplate<Long, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void publishPaymentSuccessEvent(PaymentSuccessEvent paymentSuccessEvent) {
		kafkaTemplate.send("PAYMENT_SUCCESS",paymentSuccessEvent.getPaymentId(), paymentSuccessEvent);
	}

	public void publishPaymentFailedEvent(PaymentFailedEvent paymentFailedEvent) {
		kafkaTemplate.send("PAYMENT_FAILED", paymentFailedEvent.getOrderId(), paymentFailedEvent);
	}
	
}
