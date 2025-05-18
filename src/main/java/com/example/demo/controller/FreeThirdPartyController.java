package com.example.demo.controller;

import com.example.demo.dto.tp.FreeCompanyDto;
import com.example.demo.service.FreeThirdPartyService;
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
import java.util.List;

@RestController
@RequestMapping("/free-third-party")
@Tag(name = "Free Third Party", description = "APIs for searching companies using the free third-party service")
public class FreeThirdPartyController {

    private final FreeThirdPartyService freeThirdPartyService;

    @Autowired
    public FreeThirdPartyController(FreeThirdPartyService freeThirdPartyService) {
        this.freeThirdPartyService = freeThirdPartyService;
    }

    @Operation(summary = "Search companies by CIN from Free Service", description = "Searches for companies using the free third-party service based on the provided CIN query")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Companies found successfully", content = @Content(schema = @Schema(implementation = FreeCompanyDto.class))),
            @ApiResponse(responseCode = "503", description = "Third party service unavailable", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error or error reading data", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<List<FreeCompanyDto>> getCompanies(
            @Parameter(description = "Company Identification Number (CIN) to search for", required = true) @RequestParam String query)
            throws IOException {
        List<FreeCompanyDto> result = freeThirdPartyService.searchCompanies(query);
        return ResponseEntity.ok(result);
    }
}
