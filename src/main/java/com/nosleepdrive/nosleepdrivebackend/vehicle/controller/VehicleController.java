package com.nosleepdrive.nosleepdrivebackend.vehicle.controller;

import com.nosleepdrive.nosleepdrivebackend.common.CustomError;
import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.common.SimpleResponse;
import com.nosleepdrive.nosleepdrivebackend.company.dto.CompanyDataResponseDto;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import com.nosleepdrive.nosleepdrivebackend.company.service.CompanyService;
import com.nosleepdrive.nosleepdrivebackend.vehicle.dto.DefaultVehicleDataDto;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import com.nosleepdrive.nosleepdrivebackend.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final CompanyService companyService;

    @PostMapping("")
    public ResponseEntity<SimpleResponse> addVehicle(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody DefaultVehicleDataDto request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany =  companyService.authCompany(token);
        vehicleService.createVehicle(request, curCompany);

        int customCode = HttpStatus.CREATED.value();
        SimpleResponse response = new SimpleResponse(customCode,
                Message.CREATE_VEHICLE_SUCCESS.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.valueOf(customCode))
                .body(response);
    }
}
