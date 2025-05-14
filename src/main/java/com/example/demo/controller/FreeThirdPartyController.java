package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/free-third-party")
public class FreeThirdPartyController {
    
    @Operation(summary = "Search companies by CIN from Free Service")
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getCompanies(@RequestParam String query) {
        return ResponseEntity.ok(List.of(
                Map.of(
                        "cin", "123",
                        "name", "Dummy Free Co",
                        "registration_date", "2020-01-01",
                        "address", "Free Street 1",
                        "query", query,
                        "is_active", true
                )
        ));
    }
}
