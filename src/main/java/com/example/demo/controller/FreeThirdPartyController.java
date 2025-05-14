package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;

import com.example.demo.model.FreeCompanyModel;
import com.example.demo.service.FreeThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/free-third-party")
@Tag(name = "Free Third Party", description = "APIs for searching companies using the free third-party service")
public class FreeThirdPartyController {
    
    private final FreeThirdPartyService freeThirdPartyService;

    @Autowired
    public FreeThirdPartyController(FreeThirdPartyService freeThirdPartyService) {
        this.freeThirdPartyService = freeThirdPartyService;
    }
    
    @Operation(
        summary = "Search companies by CIN from Free Service",
        description = "Searches for companies using the free third-party service based on the provided CIN query"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Companies found successfully",
            content = @Content(schema = @Schema(implementation = FreeCompanyModel.class))
        ),
        @ApiResponse(
            responseCode = "503",
            description = "Third party service unavailable",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error or error reading data",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping
    public ResponseEntity<?> getCompanies(
            @Parameter(description = "Company Identification Number (CIN) to search for", required = true)
            @RequestParam String query) {
        try {
            List<FreeCompanyModel> result = freeThirdPartyService.searchCompanies(query);
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
