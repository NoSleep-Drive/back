package com.nosleepdrive.nosleepdrivebackend.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeVehicleDto {
    @NotBlank
    @Size(max = 45)
    private String vehicleNumber;
}
