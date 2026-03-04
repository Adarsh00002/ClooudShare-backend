package com.example.cloudshareapp.Controller;

import com.example.cloudshareapp.Services.UserCredutsService;
import com.example.cloudshareapp.document.UserCredits;
import com.example.cloudshareapp.dto.UserCreditsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCreditCOntroller {

    private final UserCredutsService userCredutsService;

    @GetMapping("/credits")
    public ResponseEntity<UserCreditsDto> getUserCredit() {

        UserCredits userCredits = userCredutsService.getUserCredits();

    //    System.out.println("➡️ Sending credits to frontend: " + userCredits.getCredits());

        UserCreditsDto response = UserCreditsDto.builder()
                .credits(userCredits.getCredits())
                .plan(userCredits.getPlan())
                .build();

        return ResponseEntity.ok(response);
    }
}
