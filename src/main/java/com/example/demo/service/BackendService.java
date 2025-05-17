package com.example.demo.service;

import com.example.demo.dto.BackendServiceResponse;
import com.example.demo.dto.CompanyDto;
import com.example.demo.dto.ErrorBackendServiceResponse;
import com.example.demo.dto.SuccessBackendServiceResponse;
import com.example.demo.entity.Verification;
import com.example.demo.exception.VerificationAlreadyExistsException;
import com.example.demo.mapper.FreeCompanyMapper;
import com.example.demo.mapper.PremiumCompanyMapper;
import com.example.demo.repository.VerificationRepository;
import com.example.demo.util.SourceType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BackendService {
    private static final Logger logger = LoggerFactory.getLogger(BackendService.class);
    private final FreeThirdPartyService freeThirdPartyService;
    private final PremiumThirdPartyService premiumThirdPartyService;
    private final VerificationRepository verificationRepository;
    private final ObjectMapper objectMapper;
    private final FreeCompanyMapper freeCompanyMapper;
    private final PremiumCompanyMapper premiumCompanyMapper;

    @Autowired
    public BackendService(
            FreeThirdPartyService freeThirdPartyService,
            PremiumThirdPartyService premiumThirdPartyService,
            VerificationRepository verificationRepository,
            ObjectMapper objectMapper,
            FreeCompanyMapper freeCompanyMapper,
            PremiumCompanyMapper premiumCompanyMapper) {
        this.freeThirdPartyService = freeThirdPartyService;
        this.premiumThirdPartyService = premiumThirdPartyService;
        this.verificationRepository = verificationRepository;
        this.objectMapper = objectMapper;
        this.freeCompanyMapper = freeCompanyMapper;
        this.premiumCompanyMapper = premiumCompanyMapper;
    }

    public BackendServiceResponse searchCompanies(String query, UUID verificationId)
            throws VerificationAlreadyExistsException, IOException {

        if (verificationRepository.findById(verificationId) != null) {
            throw new VerificationAlreadyExistsException(verificationId);
        }

        // Try free service first, if it returns results return the results
        try {
            List<CompanyDto> freeResults = freeThirdPartyService.searchCompanies(query).stream()
                    .filter(company -> company.is_active())
                    .map(freeCompanyMapper::covertFreeCompanyToCompanyDto)
                    .collect(Collectors.toList());

            if (!freeResults.isEmpty()) {
                SuccessBackendServiceResponse response = createSuccessResponse(verificationId, query, freeResults);
                storeVerification(verificationId, query, response, SourceType.FREE);
                return response;
            }
        } catch (ResponseStatusException e) {
            logger.error("Free service is down, trying premium service");
        }

        // Try premium service
        try {
            List<CompanyDto> premiumResults = premiumThirdPartyService.searchCompanies(query).stream()
                    .filter(company -> company.isActive())
                    .map(premiumCompanyMapper::convertPremiumCompanyToCompanyDto)
                    .collect(Collectors.toList());

            if (!premiumResults.isEmpty()) {
                SuccessBackendServiceResponse response = createSuccessResponse(verificationId, query,
                        premiumResults);
                storeVerification(verificationId, query, response, SourceType.PREMIUM);
                return response;
            } else {
                // No results found
                ErrorBackendServiceResponse response = createErrorResponse(verificationId, query,
                        "No active companies found matching the query: " + query);
                storeVerification(verificationId, query, response, SourceType.NONE);
                return response;
            }
        } catch (ResponseStatusException e) {
            ErrorBackendServiceResponse response = createErrorResponse(verificationId, query,
                    "Both third-party services are currently unavailable");
            storeVerification(verificationId, query, response, SourceType.NONE);
            return response;
        }
    }

    private SuccessBackendServiceResponse createSuccessResponse(UUID verificationId, String query,
            List<CompanyDto> results) {
        CompanyDto firstResult = results.get(0);
        List<CompanyDto> otherResults = results.size() > 1 ? results.subList(1, results.size()) : new ArrayList<>();

        SuccessBackendServiceResponse response = new SuccessBackendServiceResponse();
        response.setVerificationId(verificationId);
        response.setQuery(query);
        response.setResult(firstResult);
        response.setOtherResults(otherResults);
        return response;
    }

    private ErrorBackendServiceResponse createErrorResponse(UUID verificationId, String query, String errorMessage) {
        ErrorBackendServiceResponse response = new ErrorBackendServiceResponse();
        response.setVerificationId(verificationId);
        response.setQuery(query);
        response.setResult(errorMessage);
        return response;
    }

    private void storeVerification(UUID verificationId, String query, BackendServiceResponse response,
            SourceType source) {
        try {
            Verification verification = Verification.builder()
                    .verificationId(verificationId)
                    .queryText(query)
                    .result(objectMapper.writeValueAsString(response))
                    .source(source)
                    .timestamp(LocalDateTime.now())
                    .build();

            verificationRepository.save(verification);
        } catch (Exception e) {
            logger.error("Failed to store verification for verificationId: {} and query: {}", verificationId, query, e);
        }
    }
}
