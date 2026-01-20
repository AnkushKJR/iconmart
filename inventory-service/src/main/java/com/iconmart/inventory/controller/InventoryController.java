package com.iconmart.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iconmart.inventory.request.ProductInventoryCreateRequest;
import com.iconmart.inventory.response.ProductInventoryCreateSuccessResponse;
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
	
}
