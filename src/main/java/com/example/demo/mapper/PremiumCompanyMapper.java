package com.example.demo.mapper;

import com.example.demo.dto.backend.CompanyDto;
import com.example.demo.dto.tp.PremiumCompanyDto;

import org.springframework.stereotype.Component;

@Component
public class PremiumCompanyMapper {

    public CompanyDto convertPremiumCompanyToCompanyDto(PremiumCompanyDto premiumCompanyDto) {
        if (premiumCompanyDto == null) {
            return null;
        }

        return CompanyDto.builder()
                .cin(premiumCompanyDto.getCompanyIdentificationNumber())
                .name(premiumCompanyDto.getCompanyName())
                .registrationDate(premiumCompanyDto.getRegistrationDate())
                .address(premiumCompanyDto.getFullAddress())
                .isActive(premiumCompanyDto.isActive())
                .build();
    }
}
