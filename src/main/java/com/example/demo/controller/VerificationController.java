package com.example.demo.controller;

import com.example.demo.dto.VerificationDto;
import com.example.demo.mapper.VerificationMapper;
import com.example.demo.repository.VerificationRepository;

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

import java.util.UUID;

@RestController
@RequestMapping("/verifications")
@Tag(name = "Verification", description = "APIs for managing verifications")
public class VerificationController {

    private final VerificationRepository verificationRepository;
    private final VerificationMapper verificationMapper;

    @Autowired
    public VerificationController(VerificationRepository repository, VerificationMapper mapper) {
        this.verificationRepository = repository;
        this.verificationMapper = mapper;
    }

    @Operation(
        summary = "Get a verification by its ID",
        description = "Retrieves a verification record by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Verification found",
            content = @Content(schema = @Schema(implementation = VerificationDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Verification not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid verification ID",
            content = @Content
        )
    })
    @GetMapping("/{verificationId}")
    public ResponseEntity<VerificationDto> getVerification(
            @Parameter(description = "ID of the verification to retrieve", required = true)
            @PathVariable UUID verificationId) {
        if (verificationId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return verificationRepository.findById(verificationId)
                .map(verificationMapper::convertVerificationToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
