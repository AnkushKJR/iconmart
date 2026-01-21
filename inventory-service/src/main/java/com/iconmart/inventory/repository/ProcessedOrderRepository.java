package com.iconmart.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iconmart.inventory.entity.ProcessedOrderEvent;

@Repository
public interface ProcessedOrderRepository extends JpaRepository<ProcessedOrderEvent, Long>{
	
	boolean existsByOrderId(Long orderId);
	
}
