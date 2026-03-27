package com.example.cloudshareapp.Services;

import com.example.cloudshareapp.Repository.PaymentTransactionRepository;
import com.example.cloudshareapp.Repository.UserCreditsRepository;
import com.example.cloudshareapp.document.PaymentTransaction;
import com.example.cloudshareapp.document.ProfileDocument;
import com.example.cloudshareapp.document.UserCredits;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCredutsService {

    private final ProfileService profileService;
    private final UserCreditsRepository userCreditsRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

 
    public UserCredits creatInitialCredits(String clerkId) {

        List<UserCredits> list = userCreditsRepository.findAllByClerkId(clerkId);

        if (!list.isEmpty()) {
            return list.get(0);
        }

        UserCredits userCredits = UserCredits.builder()
                .clerkId(clerkId)
                .credits(5)
                .plan("BASIC")
                .build();

        return userCreditsRepository.save(userCredits);
    }

    public UserCredits getUserCredits(String clerkId) {

        List<UserCredits> list = userCreditsRepository.findAllByClerkId(clerkId);

        if (list.isEmpty()) {
            System.out.println("⚠️ No credits found, creating initial credits = 5");
            return creatInitialCredits(clerkId);
        }

        if (list.size() > 1) {
            System.out.println("⚠️ Duplicate credits found, fixing...");
            userCreditsRepository.deleteAll(list.subList(1, list.size()));
        }

        return list.get(0);
    }

    public UserCredits getUserCredits() {
        String clerkId = profileService.getCurrentProfile().getClerkId();
        return getUserCredits(clerkId);
    }

    public Boolean hasEnoughtCredits(int requiedCredits) {
        UserCredits userCredits = getUserCredits();
        return userCredits.getCredits() >= requiedCredits;
    }

    public UserCredits consumeCredits() {
        UserCredits userCredits = getUserCredits();

        if (userCredits.getCredits() <= 0) {
            return null;
        }

        userCredits.setCredits(userCredits.getCredits() - 1);
        return userCreditsRepository.save(userCredits);
    }

   
    public UserCredits addCreditsAfterPayment(String clerkId, int creditsToAdd, String plan) {

        UserCredits userCredits = getUserCredits(clerkId);

        userCredits.setCredits(userCredits.getCredits() + creditsToAdd);
        userCredits.setPlan(plan);

        return userCreditsRepository.save(userCredits);
    }
}
