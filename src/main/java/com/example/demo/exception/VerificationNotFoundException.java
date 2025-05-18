package com.example.demo.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class VerificationNotFoundException extends ResponseStatusException {
    public VerificationNotFoundException(UUID verificationId) {
        super(HttpStatus.NOT_FOUND, String.format("Verification with id %s not found", verificationId));
    }
}
