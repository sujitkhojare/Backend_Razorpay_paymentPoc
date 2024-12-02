package com.razorpaypoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
	
    private String name;
    private String email;
    private String mobile;
    private int amount;
    
}