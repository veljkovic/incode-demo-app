package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Health Check", description = "APIs for checking the application health status")
public class HealthCheckController {

    @Operation(summary = "Check if the application is running", description = "Returns a simple message indicating that the application is up and running", responses = {
            @ApiResponse(responseCode = "200", description = "Application is running successfully", content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("The app is running!");
    }
}
