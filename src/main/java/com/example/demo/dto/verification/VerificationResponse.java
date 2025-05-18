package com.example.demo.dto.verification;

import java.util.UUID;

import com.example.demo.util.SourceType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VerificationSuccessResponse.class, name = "success"),
        @JsonSubTypes.Type(value = VerificationSuccessResponseWithOtherResults.class, name = "success_with_other_results"),
        @JsonSubTypes.Type(value = VerificationErrorResponse.class, name = "error")
})
public abstract class VerificationResponse {
    private UUID verificationId;
    private String queryText;
    private LocalDateTime timestamp;
    private SourceType source;
}
