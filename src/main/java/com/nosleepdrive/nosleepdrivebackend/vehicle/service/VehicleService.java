package com.nosleepdrive.nosleepdrivebackend.vehicle.service;

import com.nosleepdrive.nosleepdrivebackend.common.CustomError;
import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import com.nosleepdrive.nosleepdrivebackend.driver.repository.DriverRepository;
import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import com.nosleepdrive.nosleepdrivebackend.vehicle.dto.ChangeVehicleStatusDto;
import com.nosleepdrive.nosleepdrivebackend.vehicle.dto.DefaultVehicleDataDto;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.VehicleRepository;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    enum vehicleStatus {
        fine,
        cameraError,
        accelerationSensorError,
        speakerError,
        cameraAndAccelerationSensorError,
        speakerAndAccelerationSensorError,
        cameraAndSpeakerError,
        cameraAndSpeakerAndAccelerationSensorError,
    }
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public void createVehicle(DefaultVehicleDataDto request, Company company) {
        if (vehicleRepository.existsByIdHardware(request.getDeviceUid())||vehicleRepository.existsByCarNumber(request.getVehicleNumber())) {
            throw new CustomError(HttpStatus.CONFLICT.value(), Message.ERR_DEPLICATION_VEHICLE.getMessage());
        }
        Vehicle vehicle =  Vehicle.builder()
                .idHardware(request.getDeviceUid())
                .carNumber(request.getVehicleNumber())
                .errorState(0)
                .rentTime(null)
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
        vehicle.updateCarNumber(carNumber);
    }

    @Transactional
    public void updateVehicleStatusByDeviceUid(ChangeVehicleStatusDto request, Company company) {
        Vehicle vehicle = vehicleRepository.findByIdHardware(request.getDeviceUid());
        if(vehicle==null) {
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_NOT_FOUND_VEHICLE.getMessage());
        }
        if(vehicle.getCompany() != company){
            throw new CustomError(HttpStatus.FORBIDDEN.value(), Message.ERR_FORBIDDEN.getMessage());
        }
        int status = getStatusCodeByDeviceState(request);
        vehicle.updateErrorState(status);
    }

    private int getStatusCodeByDeviceState(ChangeVehicleStatusDto request){
        if(request.getAccelerationSensorState() && request.getSpeakerState() && request.getCameraState()){
            return vehicleStatus.fine.ordinal();
        }
        else if(request.getAccelerationSensorState() && request.getSpeakerState()){
            return vehicleStatus.cameraError.ordinal();
        }
        else if(request.getAccelerationSensorState() && request.getCameraState()){
            return vehicleStatus.speakerError.ordinal();
        }
        else if(request.getCameraState() && request.getSpeakerState()){
            return vehicleStatus.accelerationSensorError.ordinal();
        }
        else if(request.getCameraState()){
            return vehicleStatus.speakerAndAccelerationSensorError.ordinal();
        }
        else if(request.getSpeakerState()){
            return vehicleStatus.cameraAndAccelerationSensorError.ordinal();
        }
        else if(request.getAccelerationSensorState()){
            return vehicleStatus.cameraAndSpeakerError.ordinal();
        }

        return vehicleStatus.cameraAndSpeakerAndAccelerationSensorError.ordinal();
    }

    public int getAbnormalDataCount(List<Vehicle> datas) {
        int result = 0;
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getErrorState() != 0) { // 또는 다른 기준
                result++;
            }
        }

        return result;
    }

    @Transactional
    public void startRent(String vehicleNumber, Company company) {
        Vehicle vehicle = vehicleRepository.findByCarNumber(vehicleNumber);
        if(vehicle==null) {
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_NOT_FOUND_VEHICLE.getMessage());
        }
        if(vehicle.getCompany() != company){
            throw new CustomError(HttpStatus.FORBIDDEN.value(), Message.ERR_FORBIDDEN.getMessage());
        }
        if(vehicle.getRentTime() != null){
            throw new CustomError(HttpStatus.CONFLICT.value(), Message.ERR_ALREADY_RENT.getMessage());
        }


        Date curDate = new Date();
        vehicle.updateRentTime(curDate);
        vehicle.getDrivers().stream().filter(driver -> {
            return driver.getEndTime() == null;
        }).forEach(driver -> {
            driver.updateEndTime(curDate);
        });
        Driver driver =  Driver.builder()
                .startTime(curDate)
                .endTime(null)
                .vehicle(vehicle)
                .build();

        driverRepository.save(driver);
    }

    @Transactional
    public void endRent(String vehicleNumber, Company company) {
        Vehicle vehicle = vehicleRepository.findByCarNumber(vehicleNumber);
        if(vehicle==null) {
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_NOT_FOUND_VEHICLE.getMessage());
        }
        if(vehicle.getCompany() != company){
            throw new CustomError(HttpStatus.FORBIDDEN.value(), Message.ERR_FORBIDDEN.getMessage());
        }
        if(vehicle.getRentTime() == null){
            throw new CustomError(HttpStatus.CONFLICT.value(), Message.ERR_NOT_RENT.getMessage());
        }

        Date curDate = new Date();
        vehicle.updateRentTime(null);
        vehicle.getDrivers().stream()
                .filter(driver-> {
                    return driver.getEndTime() == null;
                })
                .forEach(driver -> {
                    driver.updateEndTime(curDate);
                }
        );
    }
}
