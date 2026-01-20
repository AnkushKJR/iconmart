package com.iconmart.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iconmart.cart.entity.CartItem;
import com.iconmart.cart.entity.Cart;
import java.util.List;



public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	
	Optional<CartItem> findByCartAndProductId(Cart cart, Long productId);
	
	List<CartItem> findByCart(Cart cart);
}
