package com.iconmart.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.iconmart.data.dto.ProductIncreaseReduceResponse;
import com.iconmart.data.dto.ProductStockIncreaseRequest;
import com.iconmart.data.dto.ProductStockReduceRequest;

@FeignClient(name = "INVENTORY-SERVICE", url = "http://localhost:8081/")
public interface InventoryClient {
	
	@PutMapping("/inventory/reduce")
	public ResponseEntity<ProductIncreaseReduceResponse> reduceInventory(@RequestBody ProductStockReduceRequest productStockReduceRequest);

	
	@PutMapping("/inventory/increase")
	public void increaseInventory(@RequestBody ProductStockIncreaseRequest stockIncreaseRequest);
	
}
