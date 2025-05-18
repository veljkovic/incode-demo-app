package com.example.demo.controller;

import com.example.demo.dto.tp.PremiumCompanyDto;
import com.example.demo.exception.ServiceUnavailableException;
import com.example.demo.service.PremiumThirdPartyService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PremiumThirdPartyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PremiumThirdPartyService premiumThirdPartyService;

    @InjectMocks
    private PremiumThirdPartyController premiumThirdPartyController;

    private List<PremiumCompanyDto> testCompanies;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(premiumThirdPartyController).build();
        
        testCompanies = Arrays.asList(
            PremiumCompanyDto.builder().companyIdentificationNumber("TEST123").companyName("Test Company").build(),
            PremiumCompanyDto.builder().companyIdentificationNumber("TEST456").companyName("Another Company").build(),
            PremiumCompanyDto.builder().companyIdentificationNumber("TEST789").companyName("Third Company").build()
        );
    }

    @Test
    void getCompaniesWhenServiceReturnsResultsShouldReturnCompanies() throws Exception {
        when(premiumThirdPartyService.searchCompanies(anyString())).thenReturn(testCompanies);

        mockMvc.perform(get("/premium-third-party")
                .param("query", "TEST123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyIdentificationNumber").value("TEST123"))
                .andExpect(jsonPath("$[0].companyName").value("Test Company"))
                .andExpect(jsonPath("$[1].companyIdentificationNumber").value("TEST456"))
                .andExpect(jsonPath("$[1].companyName").value("Another Company"));
    }

    @Test
    void getCompaniesWhenServiceThrowsException() throws Exception {
        when(premiumThirdPartyService.searchCompanies(anyString()))
                .thenThrow(new ServiceUnavailableException("Service error"));

        mockMvc.perform(get("/premium-third-party")
                .param("query", "TEST123"))
                .andExpect(status().isServiceUnavailable());
    }
} 