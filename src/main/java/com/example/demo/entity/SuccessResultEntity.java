package com.example.demo.entity;

import lombok.Data;

@Data
public class SuccessResultEntity implements ResultEntity<CompanyEntity> {
    private CompanyEntity result;

    @Override
    public CompanyEntity getResult() {
        return result;
    }
}
