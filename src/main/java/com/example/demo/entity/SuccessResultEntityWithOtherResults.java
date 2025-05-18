package com.example.demo.entity;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
