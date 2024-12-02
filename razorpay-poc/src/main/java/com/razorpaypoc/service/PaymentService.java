package com.razorpaypoc.service;

import java.util.List;

import com.razorpaypoc.entity.PaymentStatus;

public interface PaymentService {
	
	PaymentStatus createOrder(String name, String email, String mobile, int amount);

	PaymentStatus verifyPayment(String orderId, String paymentId, String razorpaySignature);

	List<PaymentStatus> getAllPayments();
}
