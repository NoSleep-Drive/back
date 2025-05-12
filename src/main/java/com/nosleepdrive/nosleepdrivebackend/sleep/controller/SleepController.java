package com.nosleepdrive.nosleepdrivebackend.sleep.controller;

import com.nosleepdrive.nosleepdrivebackend.common.CustomError;
import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.common.PageParam;
import com.nosleepdrive.nosleepdrivebackend.common.SimpleResponse;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import com.nosleepdrive.nosleepdrivebackend.company.service.CompanyService;
import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import com.nosleepdrive.nosleepdrivebackend.driver.service.DriverService;
import com.nosleepdrive.nosleepdrivebackend.sleep.dto.SaveVideoRequestDto;
import com.nosleepdrive.nosleepdrivebackend.sleep.dto.SleepDataDto;
import com.nosleepdrive.nosleepdrivebackend.sleep.dto.TodaySleepCountDto;
import com.nosleepdrive.nosleepdrivebackend.sleep.repository.SleepRepository;
import com.nosleepdrive.nosleepdrivebackend.sleep.service.SleepService;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import com.nosleepdrive.nosleepdrivebackend.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/sleep")
public class SleepController {
    private final VehicleService vehicleService;
    private final DriverService driverService;
    private final CompanyService companyService;
    private final SleepService sleepService;

    @PostMapping("")
    public ResponseEntity<SimpleResponse<?>> saveSleepData(@RequestHeader("Authorization") String authHeader,
                                                           @Valid SaveVideoRequestDto body) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Vehicle curVehicle = vehicleService.authVehicle(token);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            body.setDetectedAtDate(dateFormat.parse(body.getDetectedAt()));
        } catch (ParseException e) {
            throw new CustomError(HttpStatus.BAD_REQUEST.value(), Message.ERR_INVALID_INPUT.getMessage());
        }

        Driver curDriver = driverService.getDriver(curVehicle, body.getDetectedAtDate());
        sleepService.saveSleepData(curDriver, body);

        SimpleResponse<?> response = SimpleResponse.withoutData(Message.SAVE_SLEEP_DATA_SUCCESS.getMessage());
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(response);
    }

    @GetMapping("/today/count")
    public ResponseEntity<SimpleResponse<TodaySleepCountDto>> getTodaySleepCount(@RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany =  companyService.authCompany(token);
        int result = sleepService.getTodaySleepCount(curCompany);
        TodaySleepCountDto body = new TodaySleepCountDto(result);
        SimpleResponse<TodaySleepCountDto> response = SimpleResponse.withData(Message.GET_TODAY_SLEEP_COUNT_SUCCESS.getMessage(), body);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<SimpleResponse<List<SleepDataDto>>> getRecentSleepData(@RequestHeader("Authorization") String authHeader, @Valid PageParam pageData){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany =  companyService.authCompany(token);

        List<SleepDataDto> data = sleepService.getRecentSleeps(curCompany)
                .stream()
                .limit(pageData.getPageSize())
                .map(sleepOne->SleepDataDto.of(sleepOne))
                .collect(Collectors.toList());
        SimpleResponse<List<SleepDataDto>> response = SimpleResponse.withData(Message.GET_RECENT_SLEEP_DATA_SUCCESS.getMessage(), data);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }
}
