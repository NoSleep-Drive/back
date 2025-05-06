package com.nosleepdrive.nosleepdrivebackend.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VehicleNumResponseDto<T> {
    private int status;
    private String message;
    private T data;
}
