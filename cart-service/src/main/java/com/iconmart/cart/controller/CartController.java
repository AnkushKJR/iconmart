package com.iconmart.cart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iconmart.cart.dto.ProductResponseForOrder;
import com.iconmart.cart.request.AddProductRequest;
import com.iconmart.cart.response.ProductDetailsResponse;
import com.iconmart.cart.response.ProductResponse;
import com.iconmart.cart.service.CartService;

@RestController
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("/cart/add")
	public ResponseEntity<ProductResponse> addToCart(@RequestBody AddProductRequest addProductRequest) {
		ProductResponse productResponse = cartService.addProductToCart(addProductRequest);
		return ResponseEntity.ok(productResponse);
	}

	@DeleteMapping("/cart/{userId}/product/{productId}")
	public ResponseEntity<String> removeProduct(@PathVariable Long userId, @PathVariable Long productId) {
		String response = cartService.removeProductFromCart(userId, productId);
		return ResponseEntity.ok(response);
	}

	// UI
	@GetMapping("/cart/{userId}")
	public ResponseEntity<ProductDetailsResponse> getCartProducts(@PathVariable Long userId) {
		ProductDetailsResponse pdetailsResponse = cartService.getProductsFromCart(userId);
		return ResponseEntity.ok(pdetailsResponse);
	}

	// Order Service
	@GetMapping("/cart/{userId}/snapshot")
	public ResponseEntity<ProductResponseForOrder> getCartProductsForOrder(@PathVariable Long userId) {
		ProductResponseForOrder forOrder = cartService.getProductsFromCartForOrder(userId);
		return ResponseEntity.ok(forOrder);
	}

}
