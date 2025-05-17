package com.example.demo.controller;

import com.example.demo.dto.BackendServiceResponse;
import com.example.demo.exception.VerificationAlreadyExistsException;
import com.example.demo.service.BackendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/backend-service")
@Tag(name = "Backend Service", description = "APIs for company search with fallback mechanism")
public class BackendServiceController {

    private final BackendService backendService;

    @Autowired
    public BackendServiceController(BackendService backendService) {
        this.backendService = backendService;
    }

    @Operation(summary = "Search for companies", description = "Searches for companies using the free service first, falling back to premium service if needed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully", content = @Content(schema = @Schema(implementation = BackendServiceResponse.class))),
            @ApiResponse(responseCode = "503", description = "Both third-party services are unavailable", content = @Content(schema = @Schema(implementation = BackendServiceResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = BackendServiceResponse.class)))
    })
    @GetMapping
    public ResponseEntity<BackendServiceResponse> getCompanies(
            @Parameter(description = "Verification ID for tracking the request", required = true) @RequestParam UUID verificationId,
            @Parameter(description = "Search query text", required = true) @RequestParam String query)
            throws VerificationAlreadyExistsException, IOException {
        BackendServiceResponse response = backendService.searchCompanies(query, verificationId);
        return ResponseEntity.ok(response);
    }
}
