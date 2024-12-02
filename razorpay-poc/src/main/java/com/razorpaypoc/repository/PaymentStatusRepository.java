package com.razorpaypoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.razorpaypoc.entity.PaymentStatus;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
	
	PaymentStatus findByOrderId(String orderId);
}
