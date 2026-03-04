package com.example.cloudshareapp.Services;

import com.example.cloudshareapp.Repository.PaymentTransactionRepository;
import com.example.cloudshareapp.document.PaymentTransaction;
import com.example.cloudshareapp.document.ProfileDocument;
import com.example.cloudshareapp.dto.PaymentDTO;
import com.example.cloudshareapp.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ProfileService profileService;
    private final UserCredutsService userCredutsService;
    private final PaymentTransactionRepository paymentTransactionRepository;

    public PaymentDTO createFakePayment(PaymentRequest dto) {

        ProfileDocument profile = profileService.getCurrentProfile();
        String clerkId = profile.getClerkId();

        String fakeOrderId = "FAKE_ORDER_" + UUID.randomUUID();

        int credits = calculateCredits(dto.getPlan());

        PaymentTransaction transaction = PaymentTransaction.builder()
                .clerkId(clerkId)
                .orderId(fakeOrderId)
                .paymentId(dto.getPaymentId())
                .planId(dto.getPlan())
                .amount(dto.getAmount())
                .currency("INR")
                .creditAdded(credits)
                .status("SUCCESS")
                .transactionDate(LocalDateTime.now())
                .userEmail(profile.getEmail())
                .userName(profile.getFirstName() + " " + profile.getLastName())
                .build();

        paymentTransactionRepository.save(transaction);

        userCredutsService.addCreditsAfterPayment(
                clerkId,
                credits,
                dto.getPlan()
        );

        return PaymentDTO.builder()
                .success(true)
                .orderId(fakeOrderId)
                .message("Payment successful (FAKE MODE)")
                .build();
    }

    private int calculateCredits(String planId) {

        if (planId == null) return 0;

        String normalizedPlan = planId
                .trim()
                .toUpperCase()
                .replace(" PLAN", "");

        return switch (normalizedPlan) {
            case "BASIC" -> 0;
            case "PRO" -> 500;
            case "ULTIMATE" -> 2500;
            default -> 0;
        };
    }

}
