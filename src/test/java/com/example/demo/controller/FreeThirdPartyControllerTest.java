package com.example.demo.controller;

import com.example.demo.dto.tp.FreeCompanyDto;
import com.example.demo.exception.ServiceUnavailableException;
import com.example.demo.service.FreeThirdPartyService;
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
class FreeThirdPartyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FreeThirdPartyService freeThirdPartyService;

    @InjectMocks
    private FreeThirdPartyController freeThirdPartyController;

    private List<FreeCompanyDto> testCompanies;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(freeThirdPartyController).build();
        
        testCompanies = Arrays.asList(
            FreeCompanyDto.builder().cin("TEST123").name("Test Company").build(),
            FreeCompanyDto.builder().cin("TEST456").name("Another Company").build(),
            FreeCompanyDto.builder().cin("TEST789").name("Third Company").build()
        );
    }

    @Test
    void getCompaniesWhenServiceReturnsResultsShouldReturnCompanies() throws Exception {
        when(freeThirdPartyService.searchCompanies(anyString())).thenReturn(testCompanies);

        mockMvc.perform(get("/free-third-party")
                .param("query", "TEST123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cin").value("TEST123"))
                .andExpect(jsonPath("$[0].name").value("Test Company"));
    }

    @Test
    void getCompaniesWhenServiceThrowsException() throws Exception {
        when(freeThirdPartyService.searchCompanies(anyString()))
                .thenThrow(new ServiceUnavailableException("Service error"));

        mockMvc.perform(get("/free-third-party")
                .param("query", "TEST123"))
                .andExpect(status().isServiceUnavailable());
    }
} 