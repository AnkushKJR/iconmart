package com.iconmart.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.iconmart.data.dto.CartProductResponseDTO;

@FeignClient(name = "CART-SERVICE", url = "http://localhost:8082/")
public interface CartClient {
	
	@GetMapping("/cart/{userId}/snapshot")
	public ResponseEntity<CartProductResponseDTO> getCartProductsForOrder(@PathVariable Long userId);

}
