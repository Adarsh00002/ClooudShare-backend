package com.example.cloudshareapp.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {

    private String paymentId;
    private int amount;
    private String plan;     // BASIC | PRO | PREMIUM
    private String status;   // SUCCESS
}
