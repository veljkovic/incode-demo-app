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

    private UUID verificationId;
    private String queryText;
    private LocalDateTime timestamp;
    private String result;
    private SourceType source;
}
