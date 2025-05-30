package com.example.demo.dto.tp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumCompanyDto {
    private String companyIdentificationNumber;
    private String companyName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String registrationDate;

    private String fullAddress;

    @JsonProperty("isActive")
    private boolean isActive;
}
