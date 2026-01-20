package com.iconmart.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Index;

@Entity
@Table(
		name = "inventory",
		indexes = @Index(name = "idx_inventory_product_id", columnList = "product_id")
	)
public class Inventory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "product_id", nullable = false, unique = true)
	private Long productId;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	
	public Inventory() {
		
	}

	public Inventory(Long productId, Integer quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
