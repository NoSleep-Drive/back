package com.nosleepdrive.nosleepdrivebackend.sleep.controller;

import com.nosleepdrive.nosleepdrivebackend.common.*;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import com.nosleepdrive.nosleepdrivebackend.company.service.CompanyService;
import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import com.nosleepdrive.nosleepdrivebackend.driver.service.DriverService;
import com.nosleepdrive.nosleepdrivebackend.sleep.dto.*;
import com.nosleepdrive.nosleepdrivebackend.sleep.service.SleepService;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import com.nosleepdrive.nosleepdrivebackend.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/sleep")
public class SleepController {
    private final VehicleService vehicleService;
    private final DriverService driverService;
    private final CompanyService companyService;
    private final SleepService sleepService;
    private final FileFunc fileFunc;

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
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            body.setDetectedAtDate(dateFormat.parse(body.getDetectedAt()));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
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
    public ResponseEntity<SimpleResponse<TodaySleepCountDto>> getTodaySleepCount(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);
        int result = sleepService.getTodaySleepCount(curCompany);
        TodaySleepCountDto body = new TodaySleepCountDto(result);
        SimpleResponse<TodaySleepCountDto> response = SimpleResponse.withData(Message.GET_TODAY_SLEEP_COUNT_SUCCESS.getMessage(), body);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<SimpleResponse<List<SleepDataDto>>> getRecentSleepData(@RequestHeader("Authorization") String authHeader, @Valid PageParam pageData) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);

        List<SleepDataDto> data = sleepService.getRecentSleeps(curCompany)
                .stream()
                .limit(pageData.getPageSize())
                .map(sleepOne -> SleepDataDto.of(sleepOne))
                .collect(Collectors.toList());
        SimpleResponse<List<SleepDataDto>> response = SimpleResponse.withData(Message.GET_RECENT_SLEEP_DATA_SUCCESS.getMessage(), data);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @GetMapping("")
    public ResponseEntity<SimpleResponse<List<SleepDataDto>>> getSleepDataList(@RequestHeader("Authorization") String authHeader, @Valid @ModelAttribute SleepListParamDto param) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);

        List<SleepDataDto> data = sleepService.getSleepList(curCompany, param)
                .stream()
                .skip(param.getPageIdx() * param.getPageSize())
                .limit(param.getPageSize())
                .map(sleepOne -> SleepDataDto.of(sleepOne))
                .collect(Collectors.toList());
        SimpleResponse<List<SleepDataDto>> response = SimpleResponse.withData(Message.GET_SLEEP_LIST_SUCCESS.getMessage(), data);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @GetMapping("/{id}/video/stream")
    public ResponseEntity<InputStreamResource> getVideoStream(@RequestHeader("Authorization") String authHeader, @PathVariable("id") long id) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);
        File videoFile = sleepService.getSleepVideoFile(curCompany, id);
        long fileLength = videoFile.length();
        String contentType = "video/mp4";
        long rangeStart = 0;
        long rangeEnd = fileLength - 1;

        if (rangeEnd > fileLength - 1) {
            rangeEnd = fileLength - 1;
        }

        long contentLength = rangeEnd - rangeStart + 1;
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(videoFile);
            inputStream.skip(rangeStart);
        }
        catch(Exception e){
            throw new CustomError(HttpStatus.BAD_REQUEST.value(), Message.ERR_DURING_STREAM.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", contentType);
        headers.add("Accept-Ranges", "bytes");
        headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .headers(headers)
                .contentLength(contentLength)
                .body(new InputStreamResource(inputStream));
    }

    @GetMapping("/{id}/video/download")
    public ResponseEntity<Resource> downloadOneVideo(@RequestHeader("Authorization") String authHeader, @PathVariable("id") long id){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);
        File videoFile = sleepService.getSleepVideoFile(curCompany, id);

        String zipFilePath = "sleepData.zip";
        FileFunc.returnFileData zipData = fileFunc.makeOneFileToZip(zipFilePath, videoFile);


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sleepData.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(zipData.fileSize)
                .body(zipData.stream);
    }

    @PostMapping("/videos/download")
    public ResponseEntity<Resource> downloadVideos(@RequestHeader("Authorization") String authHeader, @RequestBody VideoIdsRequestDto ids){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);
        List<File> videoFile = sleepService.getSleepVideoFiles(curCompany, ids.getIds());
        String zipFilePath = "sleepData.zip";
        FileFunc.returnFileData zipData = fileFunc.makeFilesToZip(zipFilePath, videoFile);


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sleepData.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(zipData.fileSize)
                .body(zipData.stream);
    }


    @GetMapping("/count/by-driver/{driverHash}")
    public ResponseEntity<SimpleResponse<CountDto>> getCountByDriver(@RequestHeader("Authorization") String authHeader, @PathVariable String driverHash) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);
        int result = sleepService.getSleepCountByDriver(curCompany, driverHash);
        CountDto body = new CountDto(result);
        SimpleResponse<CountDto> response = SimpleResponse.withData(Message.GET_SLEEP_COUNT_SUCCESS.getMessage(), body);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);

    }

    @GetMapping("/count/by-vehicle/{vehicleNumber}")
    public ResponseEntity<SimpleResponse<CountDto>> getCountByVehicle(@RequestHeader("Authorization") String authHeader, @PathVariable String vehicleNumber) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);
        int result = sleepService.getSleepCountByVehicle(curCompany, vehicleNumber);
        CountDto body = new CountDto(result);
        SimpleResponse<CountDto> response = SimpleResponse.withData(Message.GET_SLEEP_COUNT_SUCCESS.getMessage(), body);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);

    }

    @GetMapping("/{sleepId}")
    public ResponseEntity<SimpleResponse<SleepDataDto>> getSleepData(@RequestHeader("Authorization") String authHeader, @PathVariable String sleepId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany = companyService.authCompany(token);
        SleepDataDto data = SleepDataDto.of(
                sleepService.getSleep(curCompany, Long.parseLong(sleepId))
        );
        SimpleResponse<SleepDataDto> response = SimpleResponse.withData(Message.GET_ONE_SLEEP_DATA_SUCCESS.getMessage(), data);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

}