package com.iconmart.data.dto;

public class OrderConfirmedEvent {
	
	private Long orderId;
    private Long userId;

    public OrderConfirmedEvent() {
    }

    public OrderConfirmedEvent(Long orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
