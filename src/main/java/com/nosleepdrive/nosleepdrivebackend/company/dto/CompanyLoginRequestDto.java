package com.nosleepdrive.nosleepdrivebackend.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyLoginRequestDto {
    @NotBlank
    @Size(min = 8, max = 20)
    private String id;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
}
