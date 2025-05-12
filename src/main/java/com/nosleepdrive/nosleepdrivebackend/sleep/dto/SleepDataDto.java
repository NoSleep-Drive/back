package com.nosleepdrive.nosleepdrivebackend.sleep.dto;

import com.nosleepdrive.nosleepdrivebackend.sleep.repository.entity.Sleep;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SleepDataDto {
    @NonNull
    private Long idSleep;

    @NonNull
    private String vehicleNumber;

    @NonNull
    private Date detectedTime;

    @NonNull
    private String driverHash;

    public static SleepDataDto of(Sleep sleep) {
        SleepDataDto dto = new SleepDataDto();
        dto.idSleep = sleep.getIdSleep();
        dto.vehicleNumber = sleep.getDriver().getVehicle().getCarNumber();
        dto.detectedTime = sleep.getSleepTime();
        dto.driverHash = sleep.getDriver().getDriverHash();
        return dto;
    }
}
