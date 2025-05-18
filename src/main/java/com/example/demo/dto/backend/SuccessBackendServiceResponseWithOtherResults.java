package com.example.demo.dto.backend;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessBackendServiceResponseWithOtherResults extends BackendServiceResponse {
    private CompanyDto result;
    private List<CompanyDto> otherResults;   
}
