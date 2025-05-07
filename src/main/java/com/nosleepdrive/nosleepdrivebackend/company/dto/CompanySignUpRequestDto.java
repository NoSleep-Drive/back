package com.nosleepdrive.nosleepdrivebackend.company.dto;
import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CompanySignUpRequestDto {
    @NotBlank
    @Size(min = 8, max = 20)
    private String id;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String companyName;

    @Pattern(regexp = "^[0-9]{10}$")
    @Size(min = 10, max = 10)
    private String businessNumber;
}
