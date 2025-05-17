package com.example.demo.dto;

import lombok.Data;
import java.util.UUID;

@Data
abstract public class BackendServiceResponse {
    private UUID verificationId;
    private String query;
}