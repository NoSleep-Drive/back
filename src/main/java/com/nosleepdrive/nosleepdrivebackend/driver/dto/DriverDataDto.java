package com.nosleepdrive.nosleepdrivebackend.driver.dto;

import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import lombok.Getter;

import java.util.Date;

@Getter
public class DriverDataDto {
    Date startTime;
    Date endTime;
    String driverHash;

    public static DriverDataDto of(Driver driver) {
        DriverDataDto dto = new DriverDataDto();
        dto.driverHash = driver.getDriverHash();
        dto.endTime = driver.getEndTime();
        dto.startTime = driver.getStartTime();
        return dto;
    }
}
