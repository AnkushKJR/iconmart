package com.iconmart.cart.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.iconmart.cart.dto.ProductResponseFromProduct;


@FeignClient(name = "PRODUCT-SERVICE", url = "http://localhost:8080/")
public interface ProductClient {
	
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponseFromProduct> fetchProduct(@PathVariable Long id);

}
