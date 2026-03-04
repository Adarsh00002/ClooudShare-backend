package com.example.cloudshareapp.Repository;

import com.example.cloudshareapp.document.PaymentTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentTransactionRepository
        extends MongoRepository<PaymentTransaction, String> {

    List<PaymentTransaction> findByClerkIdAndStatusOrderByTransactionDateDesc(
            String clerkId,
            String status
    );
}
