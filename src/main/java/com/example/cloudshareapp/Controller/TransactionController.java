package com.example.cloudshareapp.Controller;

import com.example.cloudshareapp.Repository.PaymentTransactionRepository;
import com.example.cloudshareapp.Services.ProfileService;
import com.example.cloudshareapp.document.PaymentTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<PaymentTransaction>> getUserTransactions() {

        String clerkId = profileService.getCurrentProfile().getClerkId();

        return ResponseEntity.ok(
                paymentTransactionRepository
                        .findByClerkIdAndStatusOrderByTransactionDateDesc(
                                clerkId, "SUCCESS"
                        )
        );
    }
}
