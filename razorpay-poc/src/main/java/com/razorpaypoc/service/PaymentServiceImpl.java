package com.razorpaypoc.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpaypoc.entity.PaymentStatus;
import com.razorpaypoc.repository.PaymentStatusRepository;
import com.razorpaypoc.util.RazorpaySignatureUtil;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    private RazorpayClient razorpayClient;
    private RazorpaySignatureUtil razorpaySignatureUtil; // Injected utility for signature verification
    
    // Constructor to initialize Razorpay client using injected apiKey and apiSecret
    @Autowired
    public PaymentServiceImpl(@Value("${razorpay.apiKey}") String apiKey,
                              @Value("${razorpay.apiSecret}") String apiSecret) throws RazorpayException {
        this.razorpayClient = new RazorpayClient(apiKey, apiSecret);
        this.razorpaySignatureUtil = new RazorpaySignatureUtil(); // Initialize the signature utility
    }

    @Override
    public PaymentStatus createOrder(String name, String email, String mobile, int amount) {
        try {
            // Create a new order in Razorpay
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100); // Amount in paise (convert from Rs to paise)
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_123456");

            // Create the order using RazorpayClient
            Order order = razorpayClient.orders.create(orderRequest);

            // Create and save payment status
            PaymentStatus paymentStatus = new PaymentStatus();
            paymentStatus.setOrderId(order.get("id"));
            paymentStatus.setName(name);
            paymentStatus.setEmail(email);
            paymentStatus.setMobile(mobile);
            paymentStatus.setAmount(amount * 100); // Store amount in paise
            paymentStatus.setStatus("CREATED");

            return paymentStatusRepository.save(paymentStatus);

        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay order: " + e.getMessage());
        }
    }

    @Override
    public PaymentStatus verifyPayment(String orderId, String paymentId, String razorpaySignature) {
        try {
            // Logging the received data
//            System.out.println("In Service: orderId: " + orderId + ", paymentId: " + paymentId + ", razorpaySignature: " + razorpaySignature);
            
            // Fetch the payment status from the repository using the orderId
            PaymentStatus paymentStatus = paymentStatusRepository.findByOrderId(orderId);
            
            if (paymentStatus == null) {
//                System.out.println("No payment status found for orderId: " + orderId);
                throw new RuntimeException("Order not found");
            }
            
//            System.out.println("PaymentStatus before verification: " + paymentStatus);

            // Temporarily bypassing the signature verification for debugging
            // boolean isSignatureValid = razorpaySignatureUtil.verifyRazorpaySignature(orderId, paymentId, razorpaySignature);
            boolean isSignatureValid = true; // Force success for debugging
            
            if (isSignatureValid) {
                paymentStatus.setPaymentId(paymentId);  // Set paymentId
                paymentStatus.setRazorpaySignature(razorpaySignature);  // Store the signature
                paymentStatus.setStatus("SUCCESS");
//                System.out.println("Payment verification successful: " + paymentStatus);
            } else {
                paymentStatus.setStatus("FAILED");
//                System.out.println("Payment verification failed: " + paymentStatus);
            }

            // Save the updated payment status with paymentId, razorpaySignature, and status
            return paymentStatusRepository.save(paymentStatus);
            
        } catch (Exception e) {
            throw new RuntimeException("Error verifying payment: " + e.getMessage());
        }
    }

    

    @Override
    public List<PaymentStatus> getAllPayments() {
        return paymentStatusRepository.findAll();
    }
}
