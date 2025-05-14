package com.example.demo.dto;

import java.util.UUID;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.demo.model.SourceType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationDto {

    @NotNull(message = "Verification ID cannot be null")
    private UUID verificationId;

    @NotBlank(message = "Query text cannot be empty")
    private String queryText;

    @NotNull(message = "Timestamp cannot be null")
    @PastOrPresent(message = "Timestamp cannot be in the future")
    private LocalDateTime timestamp;

    @NotBlank(message = "Result cannot be empty")
    private String result;

    @NotNull(message = "Source cannot be null")
    private SourceType source;
}
