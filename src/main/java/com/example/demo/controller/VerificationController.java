package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/verifications")
public class VerificationController {

    @GetMapping("/{verificationId}")
    public ResponseEntity<Map<String, Object>> getVerification(@PathVariable UUID verificationId) {
        return ResponseEntity.ok(Map.of(
                "verificationId", verificationId,
                "queryText", "dummy",
                "timestamp", "2025-05-13T12:00:00",
                "result", Map.of("cin", "123", "name", "Dummy Result Co"),
                "source", "FREE"
        ));
    }
}
