package com.example.demo.mapper;

import com.example.demo.dto.backend.CompanyDto;
import com.example.demo.dto.verification.*;
import com.example.demo.entity.*;
import com.example.demo.util.SourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VerificationMapperTest {

    @InjectMocks
    private VerificationMapper verificationMapper;

    private UUID testVerificationId;
    private CompanyDto companyDto1;
    private CompanyDto companyDto2;
    private CompanyEntity companyEntity1;
    private CompanyEntity companyEntity2;
    private VerificationEntity verificationEntity;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        testVerificationId = UUID.randomUUID();
        testTimestamp = LocalDateTime.now();

        companyDto1 = CompanyDto.builder()
                .cin("TEST123")
                .name("Test Company 1")
                .registrationDate("2024-01-01")
                .address("Test Address 1")
                .isActive(true)
                .build();

        companyDto2 = CompanyDto.builder()
                .cin("TEST456")
                .name("Test Company 2")
                .registrationDate("2024-01-02")
                .address("Test Address 2")
                .isActive(true)
                .build();

        companyEntity1 = CompanyEntity.builder()
                .cin("TEST123")
                .name("Test Company 1")
                .registrationDate("2024-01-01")
                .address("Test Address 1")
                .isActive(true)
                .build();

        companyEntity2 = CompanyEntity.builder()
                .cin("TEST456")
                .name("Test Company 2")
                .registrationDate("2024-01-02")
                .address("Test Address 2")
                .isActive(true)
                .build();

        verificationEntity = VerificationEntity.builder()
                .verificationId(testVerificationId)
                .queryText("TEST123")
                .timestamp(testTimestamp)
                .source(SourceType.FREE)
                .result(SuccessResultEntityWithOtherResults.builder()
                        .result(companyEntity1)
                        .otherResults(Arrays.asList(companyEntity2))
                        .build())
                .build();
    }

    @Test
    void convertVerificationToDtoSuccessWithOtherResults() {
        VerificationResponse response = verificationMapper.convertVerificationToDto(verificationEntity);

        assertTrue(response instanceof VerificationSuccessResponseWithOtherResults);
        VerificationSuccessResponseWithOtherResults successResponse = (VerificationSuccessResponseWithOtherResults) response;
        assertEquals(testVerificationId, successResponse.getVerificationId());
        assertEquals("TEST123", successResponse.getQueryText());
        assertEquals(testTimestamp, successResponse.getTimestamp());
        assertEquals(SourceType.FREE, successResponse.getSource());
        assertEquals(companyDto1.getCin(), successResponse.getResult().getCin());
        assertEquals(companyDto1.getName(), successResponse.getResult().getName());
        assertEquals(1, successResponse.getOtherResults().size());
        assertEquals(companyDto2.getCin(), successResponse.getOtherResults().get(0).getCin());
        assertEquals(companyDto2.getName(), successResponse.getOtherResults().get(0).getName());
    }

    @Test
    void convertVerificationToDtoWithErrorResult() {
        verificationEntity.setResult(ErrorResultEntity.builder()
                .result("Third party services unavailable")
                .build());

        VerificationResponse response = verificationMapper.convertVerificationToDto(verificationEntity);

        assertTrue(response instanceof VerificationErrorResponse);
        VerificationErrorResponse errorResponse = (VerificationErrorResponse) response;
        assertEquals(testVerificationId, errorResponse.getVerificationId());
        assertEquals("TEST123", errorResponse.getQueryText());
        assertEquals(testTimestamp, errorResponse.getTimestamp());
        assertEquals(SourceType.FREE, errorResponse.getSource());
        assertEquals("Third party services unavailable", errorResponse.getResult());
    }
}