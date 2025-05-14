package com.example.demo.mapper;

import com.example.demo.dto.VerificationDto;
import com.example.demo.model.SourceType;
import com.example.demo.entity.Verification;
import org.springframework.stereotype.Component;

@Component
public class VerificationMapper {
    
    public VerificationDto convertVerificationToDto(Verification verification) {
        if (verification == null) {
            return null;
        }
        
        return VerificationDto.builder()
                .verificationId(verification.getVerificationId())
                .queryText(verification.getQueryText())
                .timestamp(verification.getTimestamp())
                .result(verification.getResult())
                .source(SourceType.valueOf(verification.getSource().name()))
                .build();
    }
} 