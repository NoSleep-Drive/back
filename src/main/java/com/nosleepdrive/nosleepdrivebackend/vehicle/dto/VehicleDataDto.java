package com.nosleepdrive.nosleepdrivebackend.vehicle.dto;

import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VehicleDataDto {
    private String vehicleNumber;
    private String deviceUid;
    private Boolean isRented;
    private Date rentTime;
    private Date createdDate;

    public static VehicleDataDto from(Vehicle vehicle) {
        VehicleDataDto dto = new VehicleDataDto();
        dto.vehicleNumber = vehicle.getCarNumber();
        dto.deviceUid = vehicle.getIdHardware();
        dto.isRented = vehicle.getRentTime() != null;
        dto.rentTime = vehicle.getRentTime();
        dto.createdDate = vehicle.getCreatedDate();
        return dto;
    }
}
