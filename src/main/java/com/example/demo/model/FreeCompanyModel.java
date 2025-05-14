package com.example.demo.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class FreeCompanyModel {
    private String cin;
    private String name;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String registration_date;
    
    private String address;
    
    @JsonProperty("is_active")
    private boolean is_active;
}
