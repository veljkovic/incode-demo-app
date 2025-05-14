package com.example.demo.service;

import com.example.demo.model.PremiumCompanyModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PremiumThirdPartyService {
    private static final Logger logger = LoggerFactory.getLogger(PremiumThirdPartyService.class);
    private final Random random = new Random();

    public List<PremiumCompanyModel> searchCompanies(String query) throws IOException {
        try {
            logger.info("Searching for companies with query: {}", query);

            // Simulate 503 Service Unavailable 10% of the time
            if (random.nextDouble() < 0.1) {
                logger.warn("Simulating 503 Service Unavailable");
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Third party service unavailable");
            }

            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("companies_data/premium_service_companies.json");

            try {
                List<PremiumCompanyModel> companies = mapper.readValue(
                        resource.getInputStream(),
                        new TypeReference<>() {
                        });

                logger.info("Successfully read {} companies from JSON", companies.size());

                List<PremiumCompanyModel> filteredCompanies = companies.stream()
                        .filter(c -> c.getCompanyIdentificationNumber().contains(query))
                        .collect(Collectors.toList());

                return filteredCompanies;
            } catch (InvalidFormatException e) {
                logger.error("Invalid date format in JSON file: {}", e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Invalid date format in data. Expected format: yyyy-MM-dd");
            }

        } catch (ResponseStatusException e) {
            throw e;
        } catch (IOException e) {
            logger.error("Error reading or parsing JSON file: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during company search: {}", e.getMessage(), e);
            throw new IOException("Error processing company data: " + e.getMessage(), e);
        }
    }
}