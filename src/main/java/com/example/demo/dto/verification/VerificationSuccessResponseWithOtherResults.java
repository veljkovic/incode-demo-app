package com.example.demo.dto.verification;

import java.util.List;

import com.example.demo.dto.backend.CompanyDto;

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
public class VerificationSuccessResponseWithOtherResults extends VerificationResponse {
    private CompanyDto result;
    private List<CompanyDto> otherResults;
}
