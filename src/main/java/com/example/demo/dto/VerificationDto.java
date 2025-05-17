package com.example.demo.dto;

import java.util.UUID;

import com.example.demo.util.SourceType;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationDto {

    private UUID verificationId;
    private String queryText;
    private LocalDateTime timestamp;
    //TODO: maybe change type of result
    private String result;
    private SourceType source;
}
