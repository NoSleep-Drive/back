package com.nosleepdrive.nosleepdrivebackend.company.dto;

import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CompanyDataDto {
    @NotBlank
    @Size(min = 8, max = 20)
    private String id;

    @NotBlank
    @Size(max = 50)
    private String companyName;

    @Pattern(regexp = "^[0-9]{10}$")
    @Size(min = 10, max = 10)
    private String businessNumber;

    public static CompanyDataDto of(Company company) {
        CompanyDataDto dto = new CompanyDataDto();
        dto.id = company.getId();
        dto.companyName = company.getCompanyName();
        dto.businessNumber = company.getBusinessNumber();
        return dto;
    }
}
