package com.example.demo.entity;

import lombok.Data;

@Data
public class ErrorResultEntity implements ResultEntity<String> {
    private String result;

    @Override
    public String getResult() {
        return result;
    }   
}
