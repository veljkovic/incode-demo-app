package com.example.demo.entity;

import java.util.List;
import lombok.Data;

@Data
public class SuccessResultEntityWithOtherResults implements ResultEntity<CompanyEntity>  {
    private CompanyEntity result;
    private List<CompanyEntity> otherResults;

    @Override
    public CompanyEntity getResult() {
        return result;
    }

    public List<CompanyEntity> getOtherResults() {
        return otherResults;
    }
}
