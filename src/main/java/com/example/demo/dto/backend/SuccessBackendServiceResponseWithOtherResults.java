package com.example.demo.dto.backend;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessBackendServiceResponseWithOtherResults extends BackendServiceResponse {
    private CompanyDto result;
    private List<CompanyDto> otherResults;   
}
