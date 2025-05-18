package com.example.demo.dto.verification;

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
public class VerificationErrorResponse extends VerificationResponse {
    private String result;
}
