package com.nosleepdrive.nosleepdrivebackend.company.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyLoginResponseDto {
    private int status;
    private String message;
    private String token;
}
