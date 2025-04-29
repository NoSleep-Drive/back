package com.nosleepdrive.nosleepdrivebackend.company.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CompanySignUpRequestDto {
    private String id;
    private String password;
    private String companyName;
    private String businessNumber;
}
