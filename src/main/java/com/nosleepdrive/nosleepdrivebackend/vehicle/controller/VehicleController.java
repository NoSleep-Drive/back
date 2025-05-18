package com.nosleepdrive.nosleepdrivebackend.vehicle.controller;

import com.nosleepdrive.nosleepdrivebackend.common.*;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import com.nosleepdrive.nosleepdrivebackend.company.service.CompanyService;
import com.nosleepdrive.nosleepdrivebackend.driver.dto.DriverDataDto;
import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import com.nosleepdrive.nosleepdrivebackend.vehicle.dto.*;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import com.nosleepdrive.nosleepdrivebackend.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final CompanyService companyService;

    @PostMapping("")
    public ResponseEntity<SimpleResponse<?>> addVehicle(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody DefaultVehicleDataDto request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany =  companyService.authCompany(token);
        vehicleService.createVehicle(request, curCompany);

        SimpleResponse<?> response = SimpleResponse.withoutData(
                Message.CREATE_VEHICLE_SUCCESS.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(response);
    }

    @DeleteMapping("/{deviceUid}")
    public ResponseEntity<SimpleResponse<?>> deleteVehicle(@RequestHeader("Authorization") String authHeader, @PathVariable String deviceUid) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany =  companyService.authCompany(token);


        vehicleService.deleteVehicleByDeviceUid(deviceUid, curCompany);
        SimpleResponse<?> response = SimpleResponse.withoutData(
                Message.DELETE_VEHICLE_SUCCESS.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @PatchMapping("/status")
    public ResponseEntity<SimpleResponse<?>> changeVehicleStatus(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody ChangeVehicleStatusDto request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Vehicle curVehicle = vehicleService.authVehicle(token);

        vehicleService.updateVehicleStatusByDeviceUid(request, curVehicle);

        SimpleResponse<?> response = SimpleResponse.withoutData(
                Message.UPDATE_VEHICLE_STATUS_SUCCESS.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @PostMapping("/{vehicleNumber}/rent")
    public ResponseEntity<SimpleResponse<?>> rentVehicle(@RequestHeader("Authorization") String authHeader, @PathVariable String vehicleNumber) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);

        vehicleService.startRent(vehicleNumber, curCompany);

        SimpleResponse<?> response = SimpleResponse.withoutData(
                Message.RENT_SUCCESS.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @PostMapping("/{vehicleNumber}/return")
    public ResponseEntity<SimpleResponse<?>> returnVehicle(@RequestHeader("Authorization") String authHeader, @PathVariable String vehicleNumber) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);

        vehicleService.endRent(vehicleNumber, curCompany);

        SimpleResponse<?> response = SimpleResponse.withoutData(
                Message.RETURN_SUCCESS.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }


    @PatchMapping("/{deviceUid}")
    public ResponseEntity<SimpleResponse<?>> changeVehicle(@RequestHeader("Authorization") String authHeader, @PathVariable String deviceUid, @Valid @RequestBody ChangeVehicleDto request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);

        vehicleService.updateVehicleByDeviceUid(deviceUid, request.getVehicleNumber(), curCompany);
        SimpleResponse<?> response = SimpleResponse.withoutData(
                Message.UPDATE_VEHICLE_SUCCESS.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @GetMapping("")
    public ResponseEntity<SimpleResponse<List<VehicleDataDto>>> getVehicles(@RequestHeader("Authorization") String authHeader,@Valid PageParam pageData) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);

        SimpleResponse<List<VehicleDataDto>> response = SimpleResponse.withData(
                Message.GET_VEHICLES_LIST.getMessage(),
                curCompany.getVehicles()
                        .stream()
                        .skip((long) pageData.getPageIdx() * pageData.getPageSize())
                        .limit(pageData.getPageSize())
                        .map(data->VehicleDataDto.from(data))
                        .collect(Collectors.toList())
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @GetMapping("/count")
    public ResponseEntity<SimpleResponse<VehicleCountDto>> getVehicleCount(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);
        int vehicleNum = curCompany.getVehicles().size();

        int customCode = HttpStatus.OK.value();
        SimpleResponse<VehicleCountDto> response = SimpleResponse.withData(
                Message.GET_VEHICLES_COUNT_SUCCESS.getMessage()
                , new VehicleCountDto(vehicleNum));
        return ResponseEntity
                .status(HttpStatus.valueOf(customCode))
                .body(response);
    }

    @GetMapping("/abnormal/count")
    public ResponseEntity<SimpleResponse<AbnormalVehicleCountDto>> getAbnormalVehicleCount(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);
        List<Vehicle> vehicles = curCompany.getVehicles();
        int result = vehicleService.getAbnormalDataCount(vehicles);

        SimpleResponse<AbnormalVehicleCountDto> response = SimpleResponse.withData(
                Message.GET_ABNORMAL_VEHICLES_COUNT_SUCCESS.getMessage()
                , new AbnormalVehicleCountDto(result));
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @GetMapping("/{deviceUid}/drivers")
    public ResponseEntity<SimpleResponse<List<DriverDataDto>>> getDriverList(@RequestHeader("Authorization") String authHeader, @PathVariable String deviceUid,@Valid PageParam pageData) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);

        List<Driver> drivers = vehicleService.getDriverList(deviceUid, curCompany);

        SimpleResponse<List<DriverDataDto>> response = SimpleResponse.withData(
                Message.GET_DRIVERS_LIST_SUCCESS.getMessage(),
                drivers.stream()
                        .skip((long) pageData.getPageIdx() * pageData.getPageSize())
                        .limit(pageData.getPageSize())
                        .map(data->DriverDataDto.of(data))
                        .toList()
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @GetMapping("/{deviceUid}/test/getToken")
    public String getToken(@PathVariable String deviceUid) {
        return Token.generateToken(deviceUid, 60*60*24*30*12*5); // 5년
    }
}
