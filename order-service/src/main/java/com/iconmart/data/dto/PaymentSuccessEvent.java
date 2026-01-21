package com.iconmart.data.dto;

public class PaymentSuccessEvent {
	
	private String paymentId;
	private Long orderId;

    public PaymentSuccessEvent() {}

    public PaymentSuccessEvent(Long orderId, String paymentId) {
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

}
