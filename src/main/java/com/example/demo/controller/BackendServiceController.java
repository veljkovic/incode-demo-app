package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/backend-service")
public class BackendServiceController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCompanies(
            @RequestParam UUID verificationId,
            @RequestParam String query
    ) {
        return ResponseEntity.ok(Map.of(
                "verificationId", verificationId,
                "query", query,
                "otherResults", "dummy other results",
                "result", Map.of(
                        "cin", "123",
                        "name", "Dummy Result Co",
                        "is_active", true
                )
        ));
    }
}
