package com.example.demo.mapper;

import com.example.demo.dto.verification.VerificationResponse;
import com.example.demo.dto.verification.VerificationSuccessResponse;
import com.example.demo.dto.verification.VerificationSuccessResponseWithOtherResults;
import com.example.demo.dto.verification.VerificationErrorResponse;
import com.example.demo.entity.ErrorResultEntity;
import com.example.demo.entity.ResultEntity;
import com.example.demo.entity.SuccessResultEntity;
import com.example.demo.entity.SuccessResultEntityWithOtherResults;
import com.example.demo.entity.VerificationEntity;
import com.example.demo.entity.CompanyEntity;
import com.example.demo.util.SourceType;
import com.example.demo.dto.backend.CompanyDto;

import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class VerificationMapper {

    private CompanyDto mapCompanyEntityToDto(CompanyEntity entity) {
        return CompanyDto.builder()
                .cin(entity.getCin())
                .name(entity.getName())
                .registrationDate(entity.getRegistrationDate())
                .address(entity.getAddress())
                .isActive(entity.isActive())
                .build();
    }

    public VerificationResponse convertVerificationToDto(VerificationEntity verification) {
        if (verification == null) {
            return null;
        }

        ResultEntity<?> result = verification.getResult();
        
        if (result instanceof SuccessResultEntityWithOtherResults) {
            SuccessResultEntityWithOtherResults successResult = (SuccessResultEntityWithOtherResults) result;
            return VerificationSuccessResponseWithOtherResults.builder()
                    .verificationId(verification.getVerificationId())
                    .queryText(verification.getQueryText())
                    .timestamp(verification.getTimestamp())
                    .source(SourceType.valueOf(verification.getSource().name()))
                    .result(mapCompanyEntityToDto(successResult.getResult()))
                    .otherResults(successResult.getOtherResults().stream()
                            .map(this::mapCompanyEntityToDto)
                            .collect(Collectors.toList()))
                    .build();
        } else if (result instanceof SuccessResultEntity) {
            SuccessResultEntity successResult = (SuccessResultEntity) result;
            return VerificationSuccessResponse.builder()
                    .verificationId(verification.getVerificationId())
                    .queryText(verification.getQueryText())
                    .timestamp(verification.getTimestamp())
                    .source(SourceType.valueOf(verification.getSource().name()))
                    .result(mapCompanyEntityToDto(successResult.getResult()))
                    .build();
        } else if (result instanceof ErrorResultEntity) {
            ErrorResultEntity errorResult = (ErrorResultEntity) result;
            return VerificationErrorResponse.builder()
                    .verificationId(verification.getVerificationId())
                    .queryText(verification.getQueryText())
                    .timestamp(verification.getTimestamp())
                    .source(SourceType.valueOf(verification.getSource().name()))
                    .result(errorResult.getResult())
                    .build();
        }
        
        return null;
    }
}