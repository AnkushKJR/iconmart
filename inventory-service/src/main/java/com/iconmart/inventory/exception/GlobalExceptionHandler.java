package com.iconmart.inventory.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.iconmart.inventory.response.ProductInventoryCreateErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(IllegalNegativeQuantityException.class)
	public ResponseEntity<ProductInventoryCreateErrorResponse> negativeQuantity(IllegalNegativeQuantityException ex) {
		
		ProductInventoryCreateErrorResponse errorCreateResponse = new ProductInventoryCreateErrorResponse();
		errorCreateResponse.setStatus(ex.getCode() + ": " + ex.getMessage());
		
		return ResponseEntity.badRequest().body(errorCreateResponse);
	}

}
