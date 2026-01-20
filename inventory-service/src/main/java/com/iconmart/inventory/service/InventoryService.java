package com.iconmart.inventory.service;

import org.springframework.stereotype.Service;

import com.iconmart.inventory.entity.Inventory;
import com.iconmart.inventory.exception.IllegalNegativeQuantityException;
import com.iconmart.inventory.repository.InventoryRepository;
import com.iconmart.inventory.request.ProductInventoryCreateRequest;
import com.iconmart.inventory.response.ProductInventoryCreateSuccessResponse;

@Service
public class InventoryService {
	
	private final InventoryRepository inventoryRepository;
	
	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}
	
	public ProductInventoryCreateSuccessResponse generateProductInventory(ProductInventoryCreateRequest inventoryCreateRequest) {
		
		if(inventoryCreateRequest.getQuantity()<=0) {
			throw new IllegalNegativeQuantityException("Quantity cannot be less than or equal to 0");
		}
		
		inventoryRepository.findByProductId(inventoryCreateRequest.getProductId())
		.ifPresent(inv -> {
            throw new RuntimeException("Inventory already exists for this product");
        });
		
		Inventory inventory = new Inventory();
		inventory.setProductId(inventoryCreateRequest.getProductId());
		inventory.setQuantity(inventoryCreateRequest.getQuantity());
		
		Inventory persistedInventory = inventoryRepository.save(inventory);
		
		ProductInventoryCreateSuccessResponse successResponse = new ProductInventoryCreateSuccessResponse();
		successResponse.setStatus("Inventory created: " + persistedInventory.getId());
		
		return successResponse;
		
	}

}
