package com.nosleepdrive.nosleepdrivebackend.driver.service;

import com.nosleepdrive.nosleepdrivebackend.common.CustomError;
import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.driver.repository.DriverRepository;
import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;
    public Driver getDriver(Vehicle vehicle, Date date) {
        List<Driver> drivers = driverRepository.findActiveDrivers(vehicle, date);
        if(drivers.isEmpty()) {
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_SQL_NOT_FOUND.getMessage());
        }
        return drivers.getLast();
    }
}
