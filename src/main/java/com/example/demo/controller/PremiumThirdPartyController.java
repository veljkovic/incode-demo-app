package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/premium-third-party")
public class PremiumThirdPartyController {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getCompanies(@RequestParam String query) {
        return ResponseEntity.ok(List.of(
                Map.of(
                        "companyIdentificationNumber", "456",
                        "companyName", "Dummy Premium Co",
                        "registrationDate", "2019-05-05",
                        "companyFullAddress", "Premium Street 5",
                        "isActive", true,
                        "query", query
                )
        ));
    }
}
