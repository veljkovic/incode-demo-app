package com.example.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorBackendServiceResponse extends BackendServiceResponse {
    private String result;
}
