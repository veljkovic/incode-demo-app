package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.CONFLICT)
public class VerificationAlreadyExistsException extends RuntimeException {
    public VerificationAlreadyExistsException(UUID verificationId) {
        super(String.format("Verification with id %s already exists", verificationId));
    }
}