package com.example.demo.dto.tp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiumCompanyDto {
    private String companyIdentificationNumber;
    private String companyName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String registrationDate;

    private String fullAddress;

    @JsonProperty("isActive")
    private boolean isActive;
}
