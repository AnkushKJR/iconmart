package com.iconmart.cart.response;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailsResponse {
	
	private Long userId;
	private List<Product> productList;
	private BigDecimal totalPrice;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<Product> getProductList() {
		return productList;
	}
	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
