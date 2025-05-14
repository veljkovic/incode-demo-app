package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.PremiumCompanyModel;
import com.example.demo.service.PremiumThirdPartyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/premium-third-party")
@Tag(name = "Premium Third Party", description = "APIs for searching companies using the premium third-party service")
public class PremiumThirdPartyController {

    private final PremiumThirdPartyService premiumThirdPartyService;

    @Autowired
    public PremiumThirdPartyController(PremiumThirdPartyService premiumThirdPartyService) {
        this.premiumThirdPartyService = premiumThirdPartyService;
    }

    @Operation(summary = "Search companies by CIN from Premium Service", description = "Searches for companies using the premium third-party service based on the provided CIN query")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Companies found successfully", content = @Content(schema = @Schema(implementation = PremiumCompanyModel.class))),
            @ApiResponse(responseCode = "503", description = "Third party service unavailable", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error or error reading data", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<?> getCompanies(
            @Parameter(description = "Company Identification Number (CIN) to search for", required = true) @RequestParam String query) {
        try {
            List<PremiumCompanyModel> result = premiumThirdPartyService.searchCompanies(query);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("503")) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Third party service unavailable");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading data");
        }
    }
}
