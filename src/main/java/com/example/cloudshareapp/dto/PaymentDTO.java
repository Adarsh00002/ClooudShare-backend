package com.example.cloudshareapp.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {

    private boolean success;
    private String orderId;
    private String message;
}
