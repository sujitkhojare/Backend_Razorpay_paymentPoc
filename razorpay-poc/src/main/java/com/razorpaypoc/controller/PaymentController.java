package com.razorpaypoc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpaypoc.dto.PaymentRequest;
import com.razorpaypoc.entity.PaymentStatus;
import com.razorpaypoc.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000") // Adjust as per your frontend domain
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/create-order")
	public ResponseEntity<PaymentStatus> createOrder(@RequestBody PaymentRequest paymentRequest) {
		System.out.println("Received request to create order: " + paymentRequest);
		PaymentStatus paymentStatus = paymentService.createOrder(paymentRequest.getName(), paymentRequest.getEmail(),
				paymentRequest.getMobile(), paymentRequest.getAmount());
		return ResponseEntity.ok(paymentStatus);
	}

	@PostMapping("/verify-payment")
	public PaymentStatus verifyPayment(@RequestBody Map<String, String> paymentData) {
	    String orderId = paymentData.get("razorpay_order_id");
	    String paymentId = paymentData.get("razorpay_payment_id");
	    String razorpaySignature = paymentData.get("razorpay_signature");
	    
//	    System.out.println("In Controller: orderId: " + orderId + ", paymentId: " + paymentId + ", razorpaySignature: " + razorpaySignature);
	    
	    return paymentService.verifyPayment(orderId, paymentId, razorpaySignature);
	}


	@GetMapping("/payment-status")
	public List<PaymentStatus> getAllPayments() {
		return paymentService.getAllPayments();
	}
}
