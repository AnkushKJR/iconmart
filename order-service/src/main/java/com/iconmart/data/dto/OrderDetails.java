package com.iconmart.data.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.iconmart.order.enums.OrderStatus;

public class OrderDetails {
	
	private Long orderId;
	private Long userId;
	private BigDecimal totalPrice;
	private OrderStatus status;
	private LocalDateTime createdAt;
	
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
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	

}
