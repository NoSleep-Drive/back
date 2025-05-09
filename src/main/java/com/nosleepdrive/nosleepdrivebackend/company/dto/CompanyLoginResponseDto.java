package com.nosleepdrive.nosleepdrivebackend.company.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class CompanyLoginResponseDto {
    private String message;
    private String token;
}
