package com.iconmart.notification.service;

import org.springframework.stereotype.Service;

import com.iconmart.data.dto.OrderConfirmedEvent;
import com.iconmart.data.dto.OrderRejectedEvent;

@Service
public class NotificationService {

	private final EmailService emailService;

	public NotificationService(EmailService emailService) {
		this.emailService = emailService;
	}

	public void orderConfirmedMail(OrderConfirmedEvent orderConfirmedEvent) {
		emailService.sendOrderSuccessMail(orderConfirmedEvent);

	}

	public void orderRejectedMail(OrderRejectedEvent orderRejectedEvent) {
		emailService.sendOrderRejectedMail(orderRejectedEvent);
	}

}
