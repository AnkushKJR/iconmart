package com.iconmart.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iconmart.cart.dto.ProductAvailableResponseFromInventory;
import com.iconmart.cart.dto.ProductDTO;
import com.iconmart.cart.dto.ProductResponseForOrder;
import com.iconmart.cart.dto.ProductResponseFromProduct;
import com.iconmart.cart.entity.Cart;
import com.iconmart.cart.entity.CartItem;
import com.iconmart.cart.feign.InventoryClient;
import com.iconmart.cart.feign.ProductClient;
import com.iconmart.cart.repository.CartItemRepository;
import com.iconmart.cart.repository.CartRepository;
import com.iconmart.cart.request.AddProductRequest;
import com.iconmart.cart.response.Product;
import com.iconmart.cart.response.ProductDetailsResponse;
import com.iconmart.cart.response.ProductResponse;

@Service
public class CartService {
	
	private final ProductClient productClient;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final InventoryClient inventoryClient;
	
	
	public CartService(ProductClient productClient, CartRepository cartRepository, CartItemRepository cartItemRepository, InventoryClient inventoryClient) {
		this.productClient = productClient;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.inventoryClient = inventoryClient;
	}
	
	@Transactional
	public ProductResponse addProductToCart(AddProductRequest addPRequest) {

	    if (addPRequest.getQuantity() == null || addPRequest.getQuantity() <= 0) {
	        throw new RuntimeException("Quantity must be greater than 0");
	    }
	    //first cart hai ki nahi
	    Cart persistedCart = cartRepository.findByUserId(addPRequest.getUserId())
	            .orElseGet(() -> {
	                Cart cart = new Cart();
	                cart.setUserId(addPRequest.getUserId());
	                return cartRepository.save(cart);
	            });
	    
	    //Already item hai ki nahi cart mein
	    CartItem cartItem = cartItemRepository
	            .findByCartAndProductId(persistedCart, addPRequest.getProductId())
	            .orElse(new CartItem());

	    int existingQty = cartItem.getQuantity() == null ? 0 : cartItem.getQuantity();
	    int requestedQty = addPRequest.getQuantity();
	    
	    //inventory check
	    ProductAvailableResponseFromInventory inventoryResponse =
	            inventoryClient.getInventory(addPRequest.getProductId()).getBody();

	    if (inventoryResponse.getAvailableQuantity() < requestedQty) {
	        throw new RuntimeException("Insufficient stock");
	    }
	    
	    //fetch product details
	    ProductResponseFromProduct productResponse =
	            productClient.fetchProduct(addPRequest.getProductId()).getBody();

	    cartItem.setCart(persistedCart);
	    cartItem.setProductId(addPRequest.getProductId());
	    cartItem.setProductName(productResponse.getName());
	    cartItem.setPrice(productResponse.getPrice());
	    cartItem.setQuantity(existingQty + requestedQty);

	    cartItemRepository.save(cartItem);

	    return new ProductResponse("Product added to cart successfully");
	}
	
	@Transactional
	public String removeProductFromCart(Long userId, Long productId) {
		Cart persistedCart = cartRepository.findByUserId(userId)
										.orElseThrow( () -> new RuntimeException("No Cart Exists"));
		
		
		CartItem cartItem = cartItemRepository.findByCartAndProductId(persistedCart, productId)
											.orElseThrow(() -> new RuntimeException("Product not found in cart"));
		
		cartItemRepository.delete(cartItem);
		
		return "Product removed Succesfully";
	}
	
	@Transactional(readOnly = true)
	public ProductDetailsResponse getProductsFromCart(Long userId) {
		Cart persistedCart = cartRepository.findByUserId(userId)
					  					.orElseThrow(()->new RuntimeException("No cart associated by that user id"));
		
		List<CartItem> cartItems = cartItemRepository.findByCart(persistedCart);
		
		List<Product> productList = new ArrayList<>();
		BigDecimal totalPrice = BigDecimal.ZERO;
		
		for(CartItem cartItem : cartItems) {
			//Filling snapshot data [point in time data when created]
			Product product = new Product();
			product.setProductId(cartItem.getProductId());
			product.setProductName(cartItem.getProductName());
			product.setPrice(cartItem.getPrice());
			product.setQuantity(cartItem.getQuantity());
			productList.add(product);
		}
		
		ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse();
		productDetailsResponse.setUserId(userId);
		productDetailsResponse.setProductList(productList);
		
		totalPrice = productList.stream()
								.map(p-> (p.getPrice()).multiply(BigDecimal.valueOf(p.getQuantity())) )
								.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		productDetailsResponse.setTotalPrice(totalPrice);
		
		return productDetailsResponse;
	}
	
	@Transactional(readOnly = true)
	public ProductResponseForOrder getProductsFromCartForOrder(Long userId) {
		
		Cart persistedCart = cartRepository.findByUserId(userId)
										.orElseThrow(()->new RuntimeException("No cart associated by that user id"));
		
		List<CartItem> cartItems = cartItemRepository.findByCart(persistedCart);
		List<ProductDTO> productList = new ArrayList<>();
		BigDecimal totalPrice = BigDecimal.ZERO;
		
		for(CartItem cartItem : cartItems) {
			//Filling snapshot data [point in time data when created]
			ProductDTO product = new ProductDTO();
			product.setProductId(cartItem.getProductId());
			product.setPrice(cartItem.getPrice());
			product.setQuantity(cartItem.getQuantity());
			productList.add(product);
		}
		
		ProductResponseForOrder productResponseForOrder = new ProductResponseForOrder();
		productResponseForOrder.setProductList(productList);
		
		totalPrice = productList.stream()
				.map(p-> (p.getPrice()).multiply(BigDecimal.valueOf(p.getQuantity())) )
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		productResponseForOrder.setTotalPrice(totalPrice);
		
		return productResponseForOrder;
	}
	
	
}
