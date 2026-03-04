package com.example.cloudshareapp.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "payment_transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentTransaction {

    @Id
    private String id;

    private String clerkId;
    private String orderId;
    private String paymentId;
    private String planId;
    private int amount;
    private String currency;
    private int creditAdded;
    private String status;
    private LocalDateTime transactionDate;

    private String userEmail;
    private String userName;
}
