package com.iconmart.data.dto;

import java.util.List;

public class OrderRejectedEvent {
	
	private Long orderId;
    private List<OrderRejectedItem> items;

    public OrderRejectedEvent() {
    }

    public OrderRejectedEvent(Long orderId, List<OrderRejectedItem> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<OrderRejectedItem> getItems() {
        return items;
    }

    public void setItems(List<OrderRejectedItem> items) {
        this.items = items;
    }

}
