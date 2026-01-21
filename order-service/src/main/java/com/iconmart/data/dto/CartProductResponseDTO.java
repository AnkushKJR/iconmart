package com.iconmart.data.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartProductResponseDTO {
	
	private List<ProductDTO> productList;
	private BigDecimal totalPrice;
	
	public List<ProductDTO> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductDTO> productList) {
		this.productList = productList;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
