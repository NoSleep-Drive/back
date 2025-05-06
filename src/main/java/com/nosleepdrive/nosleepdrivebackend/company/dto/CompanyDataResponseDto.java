package com.nosleepdrive.nosleepdrivebackend.company.dto;

import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import lombok.Getter;

@Getter
public class CompanyDataResponseDto {
    private int status;
    private String message;
    private CompanyDataDto data;

    public CompanyDataResponseDto(int status, String message, Company company) {
        this.status = status;
        this.message = message;
        this.data = CompanyDataDto.of(company);
    }
}
