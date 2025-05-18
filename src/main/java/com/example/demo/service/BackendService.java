package com.example.demo.service;

import com.example.demo.dto.backend.BackendServiceResponse;
import com.example.demo.dto.backend.CompanyDto;
import com.example.demo.dto.backend.ErrorBackendServiceResponse;
import com.example.demo.dto.backend.SuccessBackendServiceResponse;
import com.example.demo.dto.backend.SuccessBackendServiceResponseWithOtherResults;
import com.example.demo.entity.VerificationEntity;
import com.example.demo.entity.ResultEntity;
import com.example.demo.exception.VerificationAlreadyExistsException;
import com.example.demo.mapper.FreeCompanyMapper;
import com.example.demo.mapper.PremiumCompanyMapper;
import com.example.demo.mapper.VerificationMapper;
import com.example.demo.repository.VerificationRepository;
import com.example.demo.util.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BackendService {
    private static final Logger logger = LoggerFactory.getLogger(BackendService.class);
    private final FreeThirdPartyService freeThirdPartyService;
    private final PremiumThirdPartyService premiumThirdPartyService;
    private final VerificationRepository verificationRepository;
    private final FreeCompanyMapper freeCompanyMapper;
    private final PremiumCompanyMapper premiumCompanyMapper;
    private final VerificationMapper verificationMapper;

    @Autowired
    public BackendService(
            FreeThirdPartyService freeThirdPartyService,
            PremiumThirdPartyService premiumThirdPartyService,
            VerificationRepository verificationRepository,
            FreeCompanyMapper freeCompanyMapper,
            PremiumCompanyMapper premiumCompanyMapper,
            VerificationMapper verificationMapper) {
        this.freeThirdPartyService = freeThirdPartyService;
        this.premiumThirdPartyService = premiumThirdPartyService;
        this.verificationRepository = verificationRepository;
        this.freeCompanyMapper = freeCompanyMapper;
        this.premiumCompanyMapper = premiumCompanyMapper;
        this.verificationMapper = verificationMapper;
    }

    public BackendServiceResponse searchCompanies(String query, UUID verificationId)
            throws VerificationAlreadyExistsException, IOException {

        if (verificationRepository.findById(verificationId).isPresent()) {
            throw new VerificationAlreadyExistsException(verificationId);
        }

        // Try free service first, if it returns results return the results
        try {
            List<CompanyDto> freeResults = freeThirdPartyService.searchCompanies(query).stream()
                    .filter(company -> company.is_active())
                    .map(freeCompanyMapper::covertFreeCompanyToCompanyDto)
                    .collect(Collectors.toList());

            if (!freeResults.isEmpty()) {
                BackendServiceResponse response = createSuccessResponse(verificationId, query, freeResults);
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
                BackendServiceResponse response = createSuccessResponse(verificationId, query,
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

    private BackendServiceResponse createSuccessResponse(UUID verificationId, String query,
            List<CompanyDto> results) {
        CompanyDto firstResult = results.get(0);

        if (results.size() > 1) {
            SuccessBackendServiceResponseWithOtherResults response = new SuccessBackendServiceResponseWithOtherResults();
            response.setVerificationId(verificationId);
            response.setQuery(query);
            response.setResult(firstResult);
            response.setOtherResults(results.subList(1, results.size()));
            return response;
        } else {
            SuccessBackendServiceResponse response = new SuccessBackendServiceResponse();
            response.setVerificationId(verificationId);
            response.setQuery(query);
            response.setResult(firstResult);
            return response;
        }
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
            ResultEntity<?> resultEntity = verificationMapper.mapToResultEntity(response);

            VerificationEntity verification = VerificationEntity.builder()
                    .verificationId(verificationId)
                    .queryText(query)
                    .result(resultEntity)
                    .source(source)
                    .timestamp(LocalDateTime.now())
                    .build();

            verificationRepository.save(verification);
        } catch (Exception e) {
            logger.error("Failed to store verification for verificationId: {} and query: {}", verificationId, query, e);
        }
    }
}
