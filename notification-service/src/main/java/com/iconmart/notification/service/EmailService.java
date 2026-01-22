package com.iconmart.notification.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.iconmart.data.dto.OrderConfirmedEvent;
import com.iconmart.data.dto.OrderRejectedEvent;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	//@Async
	public void sendOrderRejectedMail(OrderRejectedEvent orderRejectedEvent) {

		Long rejectedOrderId = orderRejectedEvent.getOrderId();

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setTo("akanjikar999@gmail.com");
		mailMessage.setSubject("Iconmart: Order Rejected");
		mailMessage.setText("Dear, Valuable user, your order: " + rejectedOrderId
				+ " has been rejected. Sorry for the inconvinience caused.");

		mailSender.send(mailMessage);
		
		System.out.println("order rejected mail sent...");

	}

	//@Async
	public void sendOrderSuccessMail(OrderConfirmedEvent orderConfirmedEvent) {

		Long orderId = orderConfirmedEvent.getOrderId();
		Long userId = orderConfirmedEvent.getUserId();

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setTo("akanjikar999@gmail.com");
		mailMessage.setSubject("Iconmart: Order Confirmed");
		mailMessage.setText("Dear, Valuable user: " + userId + ", your order: "+ orderId + " has been confirmed. Thankyou for shopping with us.");

		mailSender.send(mailMessage);
		
		System.out.println("order success mail sent...");

	}

}
