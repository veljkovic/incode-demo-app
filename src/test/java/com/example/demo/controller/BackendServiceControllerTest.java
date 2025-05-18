package com.example.demo.controller;

import com.example.demo.dto.backend.CompanyDto;
import com.example.demo.dto.backend.ErrorBackendServiceResponse;
import com.example.demo.dto.backend.SuccessBackendServiceResponseWithOtherResults;
import com.example.demo.exception.VerificationAlreadyExistsException;
import com.example.demo.service.BackendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BackendServiceControllerTest {

        private MockMvc mockMvc;

        @Mock
        private BackendService backendService;

        @InjectMocks
        private BackendServiceController backendServiceController;

        private UUID testVerificationId;
        private ErrorBackendServiceResponse errorResponse;
        private SuccessBackendServiceResponseWithOtherResults successWithOtherResultsResponse;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.standaloneSetup(backendServiceController).build();

                testVerificationId = UUID.randomUUID();
                CompanyDto testCompany = CompanyDto.builder()
                                .cin("TEST123")
                                .name("Test Company")
                                .registrationDate("2024-01-01")
                                .address("Test Address")
                                .isActive(true)
                                .build();

                errorResponse = ErrorBackendServiceResponse.builder()
                                .verificationId(testVerificationId)
                                .query("TEST123")
                                .result("Third party services unavailable")
                                .build();

                CompanyDto otherCompany1 = CompanyDto.builder()
                                .cin("TEST456")
                                .name("Other Company 1")
                                .registrationDate("2024-01-02")
                                .address("Other Address 1")
                                .isActive(true)
                                .build();

                CompanyDto otherCompany2 = CompanyDto.builder()
                                .cin("TEST789")
                                .name("Other Company 2")
                                .registrationDate("2024-01-03")
                                .address("Other Address 2")
                                .isActive(true)
                                .build();

                successWithOtherResultsResponse = SuccessBackendServiceResponseWithOtherResults.builder()
                                .verificationId(testVerificationId)
                                .query("TEST123")
                                .result(testCompany)
                                .otherResults(Arrays.asList(otherCompany1, otherCompany2))
                                .build();
        }

        @Test
        void getCompaniesWithOtherResults() throws Exception {
                when(backendService.searchCompanies(anyString(), any(UUID.class)))
                                .thenReturn(successWithOtherResultsResponse);

                mockMvc.perform(get("/backend-service")
                                .param("verificationId", testVerificationId.toString())
                                .param("query", "TEST123"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.verificationId").value(testVerificationId.toString()))
                                .andExpect(jsonPath("$.query").value("TEST123"))
                                .andExpect(jsonPath("$.result.cin").value("TEST123"))
                                .andExpect(jsonPath("$.result.name").value("Test Company"))
                                .andExpect(jsonPath("$.result.registrationDate").value("2024-01-01"))
                                .andExpect(jsonPath("$.result.address").value("Test Address"))
                                .andExpect(jsonPath("$.otherResults[0].cin").value("TEST456"))
                                .andExpect(jsonPath("$.otherResults[0].name").value("Other Company 1"))
                                .andExpect(jsonPath("$.otherResults[0].registrationDate").value("2024-01-02"))
                                .andExpect(jsonPath("$.otherResults[0].address").value("Other Address 1"))
                                .andExpect(jsonPath("$.otherResults[1].cin").value("TEST789"))
                                .andExpect(jsonPath("$.otherResults[1].name").value("Other Company 2"));
        }

        @Test
        void getCompaniesWithErrorResult() throws Exception {
                when(backendService.searchCompanies(anyString(), any(UUID.class))).thenReturn(errorResponse);

                mockMvc.perform(get("/backend-service")
                                .param("verificationId", testVerificationId.toString())
                                .param("query", "TEST123"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.verificationId").value(testVerificationId.toString()))
                                .andExpect(jsonPath("$.query").value("TEST123"))
                                .andExpect(jsonPath("$.result").value("Third party services unavailable"));
        }

        @Test
        void getCompaniesWhenVerificationAlreadyExists() throws Exception {
                when(backendService.searchCompanies(anyString(), any(UUID.class)))
                                .thenThrow(new VerificationAlreadyExistsException(testVerificationId));

                mockMvc.perform(get("/backend-service")
                                .param("verificationId", testVerificationId.toString())
                                .param("query", "TEST123"))
                                .andExpect(status().isConflict());
        }
}