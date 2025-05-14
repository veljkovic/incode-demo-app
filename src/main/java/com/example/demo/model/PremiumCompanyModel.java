package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiumCompanyModel {
    private String companyIdentificationNumber;
    private String companyName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String registrationDate;
    
    private String companyFullAddress;

    @JsonProperty("isActive")
    private boolean isActive;
}
