package com.iconmart.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iconmart.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	
}
