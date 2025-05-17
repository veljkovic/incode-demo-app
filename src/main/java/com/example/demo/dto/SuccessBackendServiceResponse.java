package com.example.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SuccessBackendServiceResponse extends BackendServiceResponse {
    private CompanyDto result;
    private List<CompanyDto> otherResults;
}
