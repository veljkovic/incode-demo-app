package com.example.demo.controller;

import com.example.demo.dto.verification.VerificationErrorResponse;
import com.example.demo.dto.verification.VerificationSuccessResponseWithOtherResults;
import com.example.demo.entity.CompanyEntity;
import com.example.demo.entity.SuccessResultEntityWithOtherResults;
import com.example.demo.entity.VerificationEntity;
import com.example.demo.mapper.VerificationMapper;
import com.example.demo.repository.VerificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.backend.CompanyDto;
import com.example.demo.util.SourceType;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class VerificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private VerificationMapper verificationMapper;

    @InjectMocks
    private VerificationController verificationController;

    CompanyDto companyDto1;
    CompanyDto companyDto2;
    CompanyDto companyDto3;
    CompanyEntity companyEntity1;
    CompanyEntity companyEntity2;
    CompanyEntity companyEntity3;
    private UUID testVerificationId;
    private VerificationEntity verificationEntity;
    private List<VerificationEntity> verificationEntities;
    private VerificationErrorResponse errorResponse;
    private VerificationSuccessResponseWithOtherResults successResponseWithOtherResults;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(verificationController).build();

        testVerificationId = UUID.randomUUID();

        companyDto1 = CompanyDto.builder()
                .cin("TEST123")
                .name("Test Company")
                .registrationDate("2024-01-01")
                .address("Test Address")
                .isActive(true)
                .build();

        companyDto2 = CompanyDto.builder()
                .cin("TEST456")
                .name("Other Company 1")
                .registrationDate("2024-01-02")
                .address("Other Address 1")
                .isActive(true)
                .build();

        companyDto3 = CompanyDto.builder()
                .cin("TEST789")
                .name("Other Company 2")
                .registrationDate("2024-01-03")
                .address("Other Address 2")
                .isActive(true)
                .build();

        companyEntity1 = CompanyEntity.builder()
                .cin("TEST123")
                .name("Test Company")
                .registrationDate("2024-01-01")
                .address("Test Address")
                .isActive(true)
                .build();

        companyEntity2 = CompanyEntity.builder()
                .cin("TEST456")
                .name("Other Company 1")
                .registrationDate("2024-01-02")
                .address("Other Address 1")
                .isActive(true)
                .build();

        companyEntity3 = CompanyEntity.builder()
                .cin("TEST789")
                .name("Other Company 2")
                .registrationDate("2024-01-03")
                .address("Other Address 2")
                .isActive(true)
                .build();

        errorResponse = VerificationErrorResponse.builder()
                .verificationId(testVerificationId)
                .queryText("TEST123")
                .timestamp(LocalDateTime.now())
                .source(SourceType.FREE)
                .result("Third party services unavailable")
                .build();

        successResponseWithOtherResults = VerificationSuccessResponseWithOtherResults.builder()
                .verificationId(testVerificationId)
                .queryText("TEST123")
                .timestamp(LocalDateTime.now())
                .source(SourceType.FREE)
                .result(companyDto1)
                .otherResults(Arrays.asList(companyDto2, companyDto3))
                .build();

        verificationEntity = VerificationEntity.builder()
                .verificationId(testVerificationId)
                .queryText("TEST123")
                .timestamp(LocalDateTime.now())
                .source(SourceType.FREE)
                .result(SuccessResultEntityWithOtherResults.builder().result(companyEntity1)
                        .otherResults(Arrays.asList(companyEntity2, companyEntity3)).build())
                .build();

        verificationEntities = Arrays.asList(verificationEntity);
    }

    @Test
    void getVerificationWithOtherResults() throws Exception {
        when(verificationRepository.findById(testVerificationId)).thenReturn(Optional.of(verificationEntity));
        when(verificationMapper.convertVerificationToDto(verificationEntity))
                .thenReturn(successResponseWithOtherResults);

        mockMvc.perform(get("/verifications/{verificationId}", testVerificationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verificationId").value(testVerificationId.toString()))
                .andExpect(jsonPath("$.queryText").value("TEST123"))
                .andExpect(jsonPath("$.source").value(SourceType.FREE.toString()))
                .andExpect(jsonPath("$.result.cin").value(companyDto1.getCin()))
                .andExpect(jsonPath("$.result.name").value(companyDto1.getName()))
                .andExpect(jsonPath("$.otherResults[0].cin").value(companyDto2.getCin()))
                .andExpect(jsonPath("$.otherResults[0].name").value(companyDto2.getName()))
                .andExpect(jsonPath("$.otherResults[1].cin").value(companyDto3.getCin()))
                .andExpect(jsonPath("$.otherResults[1].name").value(companyDto3.getName()));
    }

    @Test
    void getVerificationErrorResponse() throws Exception {
        when(verificationRepository.findById(testVerificationId)).thenReturn(Optional.of(verificationEntity));
        when(verificationMapper.convertVerificationToDto(verificationEntity))
                .thenReturn(errorResponse);

        mockMvc.perform(get("/verifications/{verificationId}", testVerificationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verificationId").value(testVerificationId.toString()))
                .andExpect(jsonPath("$.queryText").value("TEST123"))
                .andExpect(jsonPath("$.source").value(SourceType.FREE.toString()))
                .andExpect(jsonPath("$.result").value("Third party services unavailable"));
    }

    @Test
    void getVerificationNotFound() throws Exception {
        when(verificationRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/verifications/{verificationId}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllVerifications() throws Exception {
        when(verificationRepository.findAll()).thenReturn(verificationEntities);
        when(verificationMapper.convertVerificationToDto(verificationEntity))
                .thenReturn(successResponseWithOtherResults);

        mockMvc.perform(get("/verifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].verificationId").value(testVerificationId.toString()))
                .andExpect(jsonPath("$[0].queryText").value("TEST123"))
                .andExpect(jsonPath("$[0].source").value(SourceType.FREE.toString()))
                .andExpect(jsonPath("$[0].result.cin").value(companyDto1.getCin()))
                .andExpect(jsonPath("$[0].result.name").value(companyDto1.getName()))
                .andExpect(jsonPath("$[0].otherResults[0].cin").value(companyDto2.getCin()))
                .andExpect(jsonPath("$[0].otherResults[0].name").value(companyDto2.getName()))
                .andExpect(jsonPath("$[0].otherResults[1].cin").value(companyDto3.getCin()))
                .andExpect(jsonPath("$[0].otherResults[1].name").value(companyDto3.getName()));
    }
}