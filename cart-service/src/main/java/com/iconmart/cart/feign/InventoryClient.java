package com.iconmart.cart.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.iconmart.cart.dto.ProductAvailableResponseFromInventory;

@FeignClient(name = "INVENTORY-SERVICE", url = "http://localhost:8081/")
public interface InventoryClient {
	
	@GetMapping("/inventory/{productId}")
	public ResponseEntity<ProductAvailableResponseFromInventory> getInventory(@PathVariable Long productId);
	
}
