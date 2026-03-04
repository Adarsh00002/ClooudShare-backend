package com.example.cloudshareapp.Services;

import com.example.cloudshareapp.Repository.PaymentTransactionRepository;
import com.example.cloudshareapp.Repository.UserCreditsRepository;
import com.example.cloudshareapp.document.PaymentTransaction;
import com.example.cloudshareapp.document.ProfileDocument;
import com.example.cloudshareapp.document.UserCredits;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCredutsService {

    private final ProfileService profileService;

    private final UserCreditsRepository userCreditsRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;


    public UserCredits creatInitialCredits(String clerkId){
      UserCredits userCredits=  UserCredits.builder().
                clerkId(clerkId)
                .credits(5)
                .plan("BASIC")
                .build();

       return userCreditsRepository.save(userCredits);

    }

    public UserCredits getUserCredits(String clerkId){



        UserCredits credits = userCreditsRepository
                .findByClerkId(clerkId)
                .orElseGet(() -> {
                    System.out.println("⚠️ No credits found, creating initial credits = 5");
                    return creatInitialCredits(clerkId);
                });


        return credits;
    }


    public UserCredits getUserCredits(){
        String clerkId= profileService.getCurrentProfile().getClerkId();
        return getUserCredits(clerkId);

    }

    public Boolean hasEnoughtCredits(int requiedCredits){
       UserCredits userCredits= getUserCredits();
       return  userCredits.getCredits() >=requiedCredits;

    }

    public UserCredits consumeCredits(){
        UserCredits userCredits=getUserCredits();

        if(userCredits.getCredits() <= 0){
            return null;
        }

        userCredits.setCredits(userCredits.getCredits() - 1);
      return   userCreditsRepository.save(userCredits);
    }

    // 🔥 FAKE PAYMENT KE LIYE CREDITS ADD KARNE KA METHOD
    public UserCredits addCreditsAfterPayment(String clerkId, int creditsToAdd, String plan) {

        UserCredits userCredits = userCreditsRepository
                .findByClerkId(clerkId)
                .orElseGet(() -> creatInitialCredits(clerkId));

        userCredits.setCredits(userCredits.getCredits() + creditsToAdd);
        userCredits.setPlan(plan);

        System.out.println(userCredits.getCredits() + creditsToAdd+ " " +plan);

        return userCreditsRepository.save(userCredits);
    }




}
