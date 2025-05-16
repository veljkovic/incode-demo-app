package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class VerificationNotFoundException extends ResponseStatusException {
    public VerificationNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
