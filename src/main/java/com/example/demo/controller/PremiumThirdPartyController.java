package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.PremiumCompanyModel;
import com.example.demo.service.PremiumThirdPartyService;

import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/premium-third-party")
public class PremiumThirdPartyController {

    private final PremiumThirdPartyService premiumThirdPartyService;

    @Autowired
    public PremiumThirdPartyController(PremiumThirdPartyService premiumThirdPartyService) {
        this.premiumThirdPartyService = premiumThirdPartyService;
    }

    @Operation(summary = "Search companies by CIN from Premium Service")
    @GetMapping
    public ResponseEntity<?> getCompanies(@RequestParam String query) {
        try {
            List<PremiumCompanyModel> result = premiumThirdPartyService.searchCompanies(query);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("503")) {
                return     ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Third party service unavailable");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading data");
        }
    }
}
