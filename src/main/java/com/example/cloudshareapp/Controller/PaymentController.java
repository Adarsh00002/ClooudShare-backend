package com.example.cloudshareapp.Controller;

import com.example.cloudshareapp.Services.PaymentService;
import com.example.cloudshareapp.dto.PaymentDTO;
import com.example.cloudshareapp.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/creditAmount")
    public ResponseEntity<PaymentDTO> fakePayment(
            @RequestBody PaymentRequest req
    ) {
        if (!"SUCCESS".equals(req.getStatus())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(paymentService.createFakePayment(req));
    }
}
