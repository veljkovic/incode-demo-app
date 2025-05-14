package com.example.demo.dto;

import lombok.Data;

@Data
public class CompanyDto{
    private String cin;
    private String name;
    private String registrationDate;
    private String address;
    private boolean isActive;
}
