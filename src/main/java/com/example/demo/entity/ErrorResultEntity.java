package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResultEntity implements ResultEntity<String> {
    private String result;

    @Override
    public String getResult() {
        return result;
    }   
}
