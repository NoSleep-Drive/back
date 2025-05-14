package com.nosleepdrive.nosleepdrivebackend.sleep.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SleepListParamDto {
    @Min(0)
    private Integer pageIdx = 0;
    @Min(1)
    private Integer pageSize = 5;

    private Date startDate;
    private Date endDate;
    private String vehicleNumber;
    private String driverHash;
}
