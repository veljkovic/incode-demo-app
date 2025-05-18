package com.example.demo.dto.backend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SuccessBackendServiceResponse.class, name = "success"),
    @JsonSubTypes.Type(value = SuccessBackendServiceResponseWithOtherResults.class, name = "success_with_other_results"),
    @JsonSubTypes.Type(value = ErrorBackendServiceResponse.class, name = "error")
})
abstract public class BackendServiceResponse {
    private UUID verificationId;
    private String query;
}