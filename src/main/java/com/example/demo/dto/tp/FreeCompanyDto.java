package com.example.demo.dto.tp;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeCompanyDto {
    private String cin;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String registration_date;

    private String address;

    @JsonProperty("is_active")
    private boolean is_active;
}
