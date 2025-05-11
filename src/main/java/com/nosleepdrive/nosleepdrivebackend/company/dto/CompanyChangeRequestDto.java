package com.nosleepdrive.nosleepdrivebackend.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyChangeRequestDto {
    @Size(min = 8, max = 20)
    private String password;

    @Size(max = 50)
    private String companyName;

    @Pattern(regexp = "^[0-9]{10}$")
    @Size(min = 10, max = 10)
    private String businessNumber;
}
