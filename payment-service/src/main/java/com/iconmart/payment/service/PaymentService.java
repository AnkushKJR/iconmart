package com.iconmart.payment.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iconmart.data.dto.OrderCreatedEvent;
import com.iconmart.data.dto.PaymentFailedEvent;
import com.iconmart.data.dto.PaymentSuccessEvent;
import com.iconmart.payment.entity.Payment;
import com.iconmart.payment.enums.PaymentStatus;
import com.iconmart.payment.repository.PaymentRepository;

@Service
public class PaymentService {
	
	private final PaymentRepository paymentRepository;
	private final PaymentProducer paymentProducer;
	
	public PaymentService(PaymentRepository paymentRepository, PaymentProducer paymentProducer) {
		this.paymentRepository = paymentRepository;
		this.paymentProducer = paymentProducer;
	}
	
	@Transactional
	public void initiatePayment(OrderCreatedEvent orderCreatedEvent) {
		
		if (paymentRepository.existsByOrderId(orderCreatedEvent.getOrderId())) {
	        return;
	    }
		
		Payment payment = new Payment();
		payment.setOrderId(orderCreatedEvent.getOrderId());
		payment.setUserId(orderCreatedEvent.getUserId());
		payment.setAmount(orderCreatedEvent.getTotalAmount());
		payment.setStatus(PaymentStatus.INITIATED);
		
		String paymentRefId = UUID.randomUUID().toString();
		
		payment.setPaymentReferenceId(paymentRefId);
		
		Payment savedPayment = paymentRepository.save(payment);
		
		// ✅ Mock payment decision
	    boolean success = savedPayment.getAmount().compareTo(BigDecimal.valueOf(5000)) < 0;
		
		
	    if (success) {
	    	
	        savedPayment.setStatus(PaymentStatus.SUCCESS);
	        Payment persistedPayment = paymentRepository.save(savedPayment);
	        
	        PaymentSuccessEvent paymentSuccessEvent = new PaymentSuccessEvent();
	        paymentSuccessEvent.setPaymentId(persistedPayment.getPaymentId());
	        paymentSuccessEvent.setOrderId(persistedPayment.getOrderId());
	        
	        paymentProducer.publishPaymentSuccessEvent(paymentSuccessEvent);
	        
	    } else {
	    	
	        savedPayment.setStatus(PaymentStatus.FAILED);
	        Payment persistedPayment = paymentRepository.save(savedPayment);
	        
	        PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent();
	        paymentFailedEvent.setOrderId(persistedPayment.getOrderId());
	        paymentFailedEvent.setReason("Payment Failed due payment gateway issue. try again after some time.");
	        
	        paymentProducer.publishPaymentFailedEvent(paymentFailedEvent);
	        
	    }
	    
	}
	
	

}
