package com.example.demo.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private String cin;
    private String name;
    private String registrationDate;
    private String address;
    private boolean isActive;
}
