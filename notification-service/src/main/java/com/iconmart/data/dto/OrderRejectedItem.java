package com.iconmart.data.dto;

public class OrderRejectedItem {
	
	private Long productId;
    private Integer quantity;

    public OrderRejectedItem() {
    }

    public OrderRejectedItem(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
