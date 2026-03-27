package com.example.cloudshareapp.Repository;

import com.example.cloudshareapp.document.UserCredits;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UserCreditsRepository extends MongoRepository<UserCredits,String> {


    List<UserCredits> findAllByClerkId(String clerkId);
}
