package com.nosleepdrive.nosleepdrivebackend.vehicle.dto;

import com.nosleepdrive.nosleepdrivebackend.company.dto.CompanyDataDto;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class VehicleListResponseDto {
    private int status;
    private String message;
    private List<VehicleDataDto> data;

    public VehicleListResponseDto(int status, String message, List<Vehicle> vehicles) {
        this.status = status;
        this.message = message;
        this.data = vehicles.stream().map(data-> VehicleDataDto.from(data)).collect(Collectors.toList());
    }
}
