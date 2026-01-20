package com.iconmart.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iconmart.inventory.entity.Inventory;
import com.iconmart.inventory.exception.IllegalNegativeQuantityException;
import com.iconmart.inventory.repository.InventoryRepository;
import com.iconmart.inventory.request.ProductInventoryCreateRequest;
import com.iconmart.inventory.request.ProductStockIncreaseRequest;
import com.iconmart.inventory.request.ProductStockReduceRequest;
import com.iconmart.inventory.response.ProductAvailableSuccessResponse;
import com.iconmart.inventory.response.ProductIncreaseSuccessResponse;
import com.iconmart.inventory.response.ProductInventoryCreateSuccessResponse;
import com.iconmart.inventory.response.ProductReduceSuccessResponse;

@Service
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	public ProductInventoryCreateSuccessResponse generateProductInventory(
			ProductInventoryCreateRequest inventoryCreateRequest) {

		if (inventoryCreateRequest.getQuantity() <= 0) {
			throw new IllegalNegativeQuantityException("Quantity cannot be less than or equal to 0");
		}

		inventoryRepository.findByProductId(inventoryCreateRequest.getProductId()).ifPresent(inv -> {
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

	@Transactional(readOnly = true)
	public ProductAvailableSuccessResponse checkAvailability(Long productId) {

		Inventory productInInventory = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new RuntimeException("No product by given product id"));

		ProductAvailableSuccessResponse pAvailableSuccessResponse = new ProductAvailableSuccessResponse();

		pAvailableSuccessResponse.setProductId(productInInventory.getProductId());
		pAvailableSuccessResponse.setAvailableQuantity(productInInventory.getQuantity());

		return pAvailableSuccessResponse;
	}

	@Transactional
	public ProductReduceSuccessResponse reduceStock(ProductStockReduceRequest stockReduceRequest) {

		if (stockReduceRequest.getQuantity() <= 0) {
			throw new RuntimeException("Requested Quantity should not be less than 0");
		}

		// Availability Check
		Inventory productInInventory = inventoryRepository.findByProductId(stockReduceRequest.getProductId())
				.orElseThrow(() -> new RuntimeException("No product by given product id"));

		if (productInInventory.getQuantity() < stockReduceRequest.getQuantity()) {
			throw new RuntimeException("not enough stock");
		}

		// Reduction
		Integer quantity = productInInventory.getQuantity() - stockReduceRequest.getQuantity();
		productInInventory.setQuantity(quantity);

		// Save is not required for managed entities inside a transaction
		inventoryRepository.save(productInInventory);

		ProductReduceSuccessResponse reduceSuccessResponse = new ProductReduceSuccessResponse(
				"Stock reduction SUCCESSFUL");

		return reduceSuccessResponse;

	}

	@Transactional
	public ProductIncreaseSuccessResponse increaseStock(ProductStockIncreaseRequest stockIncreaseRequest) {

		if (stockIncreaseRequest.getQuantity() <= 0) {
			throw new RuntimeException("Requested Quantity should not be less than 0");
		}

		// Availability Check
		Inventory productInInventory = inventoryRepository.findByProductId(stockIncreaseRequest.getProductId())
				.orElseThrow(() -> new RuntimeException("No product by given product id"));

		// Increase
		Integer quantity = productInInventory.getQuantity() + stockIncreaseRequest.getQuantity();
		productInInventory.setQuantity(quantity);

		// Save is not required for managed entities inside a transaction
		inventoryRepository.save(productInInventory);
		
		ProductIncreaseSuccessResponse increaseSuccessResponse = new ProductIncreaseSuccessResponse("Stock increase SUCCESSFUL");
		
		return increaseSuccessResponse;
	}

}
