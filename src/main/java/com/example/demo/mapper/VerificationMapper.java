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
import com.example.demo.dto.backend.BackendServiceResponse;
import com.example.demo.dto.backend.ErrorBackendServiceResponse;
import com.example.demo.dto.backend.SuccessBackendServiceResponse;
import com.example.demo.dto.backend.SuccessBackendServiceResponseWithOtherResults;

import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import java.util.List;

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

    public ResultEntity<?> mapToResultEntity(BackendServiceResponse response) {
        if (response instanceof SuccessBackendServiceResponseWithOtherResults) {
            return mapToSuccessResultWithOtherResults((SuccessBackendServiceResponseWithOtherResults) response);
        } else if (response instanceof SuccessBackendServiceResponse) {
            return mapToSuccessResult((SuccessBackendServiceResponse) response);
        } else if (response instanceof ErrorBackendServiceResponse) {
            return mapToErrorResult((ErrorBackendServiceResponse) response);
        } else {
            throw new IllegalArgumentException("Unknown response type: " + response.getClass().getName());
        }
    }

    private SuccessResultEntityWithOtherResults mapToSuccessResultWithOtherResults(SuccessBackendServiceResponseWithOtherResults response) {
        CompanyEntity mainResult = mapToCompanyEntity(response.getResult());
        List<CompanyEntity> otherResults = response.getOtherResults().stream()
                .map(this::mapToCompanyEntity)
                .collect(Collectors.toList());

        SuccessResultEntityWithOtherResults successResultEntity = new SuccessResultEntityWithOtherResults();
        successResultEntity.setResult(mainResult);
        successResultEntity.setOtherResults(otherResults);
        return successResultEntity;
    }

    private SuccessResultEntity mapToSuccessResult(SuccessBackendServiceResponse response) {
        CompanyEntity companyEntity = mapToCompanyEntity(response.getResult());
        SuccessResultEntity successResultEntity = new SuccessResultEntity();
        successResultEntity.setResult(companyEntity);
        return successResultEntity;
    }

    private ErrorResultEntity mapToErrorResult(ErrorBackendServiceResponse response) {
        ErrorResultEntity errorResultEntity = new ErrorResultEntity();
        errorResultEntity.setResult(response.getResult());
        return errorResultEntity;
    }

    private CompanyEntity mapToCompanyEntity(CompanyDto dto) {
        return CompanyEntity.builder()
                .cin(dto.getCin())
                .name(dto.getName())
                .registrationDate(dto.getRegistrationDate())
                .address(dto.getAddress())
                .isActive(dto.isActive())
                .build();
    }
}