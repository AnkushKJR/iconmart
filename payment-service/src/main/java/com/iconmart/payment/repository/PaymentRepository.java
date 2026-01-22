package com.iconmart.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iconmart.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

	boolean existsByOrderId(Long orderId);

}
