package com.example.demo.service;

import com.example.demo.dto.backend.*;
import com.example.demo.dto.tp.FreeCompanyDto;
import com.example.demo.dto.tp.PremiumCompanyDto;
import com.example.demo.entity.*;
import com.example.demo.mapper.FreeCompanyMapper;
import com.example.demo.mapper.PremiumCompanyMapper;
import com.example.demo.mapper.VerificationMapper;
import com.example.demo.repository.VerificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BackendServiceTest {

    @Mock
    private FreeThirdPartyService freeThirdPartyService;

    @Mock
    private PremiumThirdPartyService premiumThirdPartyService;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private FreeCompanyMapper freeCompanyMapper;

    @Mock
    private PremiumCompanyMapper premiumCompanyMapper;

    @Mock
    private VerificationMapper verificationMapper;

    @InjectMocks
    private BackendService backendService;

    private UUID testVerificationId;
    private CompanyDto companyDto1;
    private CompanyDto companyDto2;
    private FreeCompanyDto freeCompanyDto1;
    private FreeCompanyDto freeCompanyDto2;
    private PremiumCompanyDto premiumCompanyDto1;
    private PremiumCompanyDto premiumCompanyDto2;

    @BeforeEach
    void setUp() {
        testVerificationId = UUID.randomUUID();

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

        freeCompanyDto1 = new FreeCompanyDto("TEST123", "Test Company 1", "2024-01-01", "Test Address 1", true);
        freeCompanyDto2 = new FreeCompanyDto("TEST456", "Test Company 2", "2024-01-02", "Test Address 2", true);
        premiumCompanyDto1 = new PremiumCompanyDto("TEST123", "Test Company 1", "2024-01-01", "Test Address 1", true);
        premiumCompanyDto2 = new PremiumCompanyDto("TEST456", "Test Company 2", "2024-01-02", "Test Address 2", true);
    }

    @Test
    void searchCompaniesSuccessWithFreeService() throws IOException {
        when(verificationRepository.findById(testVerificationId)).thenReturn(Optional.empty());
        when(freeThirdPartyService.searchCompanies("TEST123"))
                .thenReturn(Arrays.asList(freeCompanyDto1, freeCompanyDto2));
        when(freeCompanyMapper.covertFreeCompanyToCompanyDto(freeCompanyDto1)).thenReturn(companyDto1);
        when(freeCompanyMapper.covertFreeCompanyToCompanyDto(freeCompanyDto2)).thenReturn(companyDto2);

        BackendServiceResponse response = backendService.searchCompanies("TEST123", testVerificationId);

        assertTrue(response instanceof SuccessBackendServiceResponseWithOtherResults);
        SuccessBackendServiceResponseWithOtherResults successResponse = (SuccessBackendServiceResponseWithOtherResults) response;
        assertEquals(testVerificationId, successResponse.getVerificationId());
        assertEquals("TEST123", successResponse.getQuery());
        assertEquals(companyDto1, successResponse.getResult());
        assertEquals(1, successResponse.getOtherResults().size());
        assertEquals(companyDto2, successResponse.getOtherResults().get(0));

        verify(verificationRepository).save(any(VerificationEntity.class));
    }

    @Test
    void searchCompaniesSuccessWithPremiumService() throws IOException {
        when(verificationRepository.findById(testVerificationId)).thenReturn(Optional.empty());
        when(freeThirdPartyService.searchCompanies("TEST123"))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE));
        when(premiumThirdPartyService.searchCompanies("TEST123"))
                .thenReturn(Arrays.asList(premiumCompanyDto1, premiumCompanyDto2));
        when(premiumCompanyMapper.convertPremiumCompanyToCompanyDto(premiumCompanyDto1)).thenReturn(companyDto1);
        when(premiumCompanyMapper.convertPremiumCompanyToCompanyDto(premiumCompanyDto2)).thenReturn(companyDto2);

        BackendServiceResponse response = backendService.searchCompanies("TEST123", testVerificationId);

        assertTrue(response instanceof SuccessBackendServiceResponseWithOtherResults);
        SuccessBackendServiceResponseWithOtherResults successResponse = (SuccessBackendServiceResponseWithOtherResults) response;
        assertEquals(testVerificationId, successResponse.getVerificationId());
        assertEquals("TEST123", successResponse.getQuery());
        assertEquals(companyDto1, successResponse.getResult());
        assertEquals(1, successResponse.getOtherResults().size());
        assertEquals(companyDto2, successResponse.getOtherResults().get(0));

        verify(verificationRepository).save(any(VerificationEntity.class));
    }

    @Test
    void searchCompaniesErrorWhenBothServicesUnavailable() throws IOException {
        when(verificationRepository.findById(testVerificationId)).thenReturn(Optional.empty());
        when(freeThirdPartyService.searchCompanies("TEST123"))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE));
        when(premiumThirdPartyService.searchCompanies("TEST123"))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE));

        BackendServiceResponse response = backendService.searchCompanies("TEST123", testVerificationId);

        assertTrue(response instanceof ErrorBackendServiceResponse);
        ErrorBackendServiceResponse errorResponse = (ErrorBackendServiceResponse) response;
        assertEquals(testVerificationId, errorResponse.getVerificationId());
        assertEquals("TEST123", errorResponse.getQuery());
        assertEquals("Both third-party services are currently unavailable", errorResponse.getResult());

        verify(verificationRepository).save(any(VerificationEntity.class));
    }
}