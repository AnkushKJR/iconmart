package com.iconmart.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iconmart.data.dto.ProductStockIncreaseRequest;
import com.iconmart.inventory.request.ProductInventoryCreateRequest;

import com.iconmart.inventory.request.ProductStockReduceRequest;
import com.iconmart.inventory.response.ProductAvailableSuccessResponse;
import com.iconmart.inventory.response.ProductIncreaseSuccessResponse;
import com.iconmart.inventory.response.ProductInventoryCreateSuccessResponse;
import com.iconmart.inventory.response.ProductReduceSuccessResponse;
import com.iconmart.inventory.service.InventoryService;

@RestController
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
	
	@PostMapping("/inventory")
	public ResponseEntity<ProductInventoryCreateSuccessResponse> createProductInventory(@RequestBody ProductInventoryCreateRequest createRequest) {
		ProductInventoryCreateSuccessResponse successResponse = inventoryService.generateProductInventory(createRequest);
		return ResponseEntity.ok(successResponse);
	}
	
	@GetMapping("/inventory/{productId}")
	public ResponseEntity<ProductAvailableSuccessResponse> getInventory(@PathVariable Long productId){
		ProductAvailableSuccessResponse successResponse = inventoryService.checkAvailability(productId);
		return ResponseEntity.ok(successResponse);
	}
	
	@PutMapping("/inventory/reduce")
	public ResponseEntity<ProductReduceSuccessResponse> reduceInventory(@RequestBody ProductStockReduceRequest productStockReduceRequest) {
		ProductReduceSuccessResponse reduceSuccessResponse = inventoryService.reduceStock(productStockReduceRequest);
		return ResponseEntity.ok(reduceSuccessResponse);
	}
	
	@PutMapping("/inventory/increase")
	public void increaseInventory(@RequestBody ProductStockIncreaseRequest stockIncreaseRequest) {
		inventoryService.increaseStock(stockIncreaseRequest);
//		return ResponseEntity.ok(increaseSuccessResponse);
	}
	
	
}
