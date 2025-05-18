package com.example.demo.service;

import com.example.demo.dto.tp.FreeCompanyDto;
import com.example.demo.exception.ServiceUnavailableException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class FreeThirdPartyService {

    private static final Logger logger = LoggerFactory.getLogger(FreeThirdPartyService.class);
    private final Random random = new Random();
    private final ObjectMapper objectMapper;

    @Autowired
    public FreeThirdPartyService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<FreeCompanyDto> searchCompanies(String query) throws IOException {
        logger.info("Searching for companies with query: {}", query);

        // Simulate 503 Service Unavailable 40% of the time
        if (random.nextDouble() < 0.4) {
            logger.warn("Simulating 503 Service Unavailable");
            throw new ServiceUnavailableException("Free third-party service is currently unavailable");
        }

        ClassPathResource resource = new ClassPathResource("companies_data/free_service_companies.json");

        List<FreeCompanyDto> companies = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<>() {
                });

        logger.info("Successfully read {} companies from JSON", companies.size());

        List<FreeCompanyDto> filteredCompanies = companies.stream()
                .filter(c -> c.getCin().contains(query))
                .collect(Collectors.toList());

        return filteredCompanies;
    }
}
