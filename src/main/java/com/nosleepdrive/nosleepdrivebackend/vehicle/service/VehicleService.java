package com.nosleepdrive.nosleepdrivebackend.vehicle.service;

import com.nosleepdrive.nosleepdrivebackend.common.CustomError;
import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import com.nosleepdrive.nosleepdrivebackend.vehicle.dto.DefaultVehicleDataDto;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.VehicleRepository;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public void createVehicle(DefaultVehicleDataDto request, Company company) {
        if (vehicleRepository.existsByIdHardware(request.getDeviceUid())||vehicleRepository.existsByCarNumber(request.getVehicleNumber())) {
            throw new CustomError(HttpStatus.CONFLICT.value(), Message.ERR_DEPLICATION_VEHICLE.getMessage());
        }
        Vehicle vehicle =  Vehicle.builder()
                .idHardware(request.getDeviceUid())
                .carNumber(request.getVehicleNumber())
                .errorState(0)
                .createdDate(new Date())
                .company(company)
                .build();

        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void deleteVehicleByDeviceUid(String deviceUid, Company company) {
        Vehicle vehicle = vehicleRepository.findByIdHardware(deviceUid);
        if(vehicle==null) {
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_NOT_FOUND_VEHICLE.getMessage());
        }
        if(vehicle.getCompany() != company){
            throw new CustomError(HttpStatus.FORBIDDEN.value(), Message.ERR_FORBIDDEN.getMessage());
        }
        vehicleRepository.delete(vehicle);
    }

    @Transactional
    public void updateVehicleByDeviceUid(String deviceUid, String carNumber, Company company) {
        Vehicle vehicle = vehicleRepository.findByIdHardware(deviceUid);
        if(vehicle==null) {
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_NOT_FOUND_VEHICLE.getMessage());
        }
        if(vehicle.getCompany() != company){
            throw new CustomError(HttpStatus.FORBIDDEN.value(), Message.ERR_FORBIDDEN.getMessage());
        }
        vehicle.setCarNumber(carNumber);
    }
}
