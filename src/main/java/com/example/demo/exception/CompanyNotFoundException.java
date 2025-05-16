package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CompanyNotFoundException extends ResponseStatusException {
    public CompanyNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
} 