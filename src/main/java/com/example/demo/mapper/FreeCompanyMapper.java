package com.example.demo.mapper;

import com.example.demo.dto.backend.CompanyDto;
import com.example.demo.dto.tp.FreeCompanyDto;

import org.springframework.stereotype.Component;

@Component
public class FreeCompanyMapper {

    public CompanyDto covertFreeCompanyToCompanyDto(FreeCompanyDto freeCompanyDto) {
        if (freeCompanyDto == null) {
            return null;
        }

        return CompanyDto.builder()
                .cin(freeCompanyDto.getCin())
                .name(freeCompanyDto.getName())
                .registrationDate(freeCompanyDto.getRegistration_date())
                .address(freeCompanyDto.getAddress())
                .isActive(freeCompanyDto.is_active())
                .build();
    }
}
