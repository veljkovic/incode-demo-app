package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;
import java.util.List;

import com.example.demo.model.FreeCompanyModel;
import com.example.demo.service.FreeThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/free-third-party")
public class FreeThirdPartyController {
    
    private final FreeThirdPartyService freeThirdPartyService;

    @Autowired
    public FreeThirdPartyController(FreeThirdPartyService freeThirdPartyService) {
        this.freeThirdPartyService = freeThirdPartyService;
    }
    
    @Operation(summary = "Search companies by CIN from Free Service")
    @GetMapping
    public ResponseEntity<?> getCompanies(@RequestParam String query) {
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
