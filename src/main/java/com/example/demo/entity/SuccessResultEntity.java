package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResultEntity implements ResultEntity<CompanyEntity> {
    private CompanyEntity result;

    @Override
    public CompanyEntity getResult() {
        return result;
    }
}
