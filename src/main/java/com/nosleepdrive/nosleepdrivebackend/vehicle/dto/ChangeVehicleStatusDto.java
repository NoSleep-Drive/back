package com.nosleepdrive.nosleepdrivebackend.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeVehicleStatusDto {
    @NotBlank
    @Size(max = 50)
    private String deviceUid;

    @NotNull
    private Boolean cameraState;

    @NotNull
    private Boolean accelerationSensorState;

    @NotNull
    private Boolean speakerState;
}
