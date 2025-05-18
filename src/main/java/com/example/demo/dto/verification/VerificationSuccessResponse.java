package com.example.demo.dto.verification;

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
public class VerificationSuccessResponse extends VerificationResponse {
    private CompanyDto result;
}
