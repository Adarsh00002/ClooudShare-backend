package com.example.cloudshareapp.Exception;

import com.mongodb.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicatieEmailException(DuplicateKeyException ex){
        Map<String, Object> data=new HashMap<>();
        data.put("status",ex.getMessage());
        data.put("message",ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(data);
    }
}
