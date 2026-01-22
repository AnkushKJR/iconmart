package com.iconmart.data.dto;

public class PaymentSuccessEvent {
	
	private Long paymentId;
	private Long orderId;

    public PaymentSuccessEvent() {}

    public PaymentSuccessEvent(Long orderId, Long paymentId) {
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	

}
