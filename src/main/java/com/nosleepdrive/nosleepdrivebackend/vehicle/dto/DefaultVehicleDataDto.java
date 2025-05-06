package com.nosleepdrive.nosleepdrivebackend.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
public class DefaultVehicleDataDto {
    @NotBlank
    @Size(max = 45)
    private String vehicleNumber;

    @NotBlank
    @Size(max = 50)
    private String deviceUid;
}
