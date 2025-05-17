package com.nosleepdrive.nosleepdrivebackend.sleep.service;

import com.nosleepdrive.nosleepdrivebackend.common.CustomError;
import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.common.SimpleResponse;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import com.nosleepdrive.nosleepdrivebackend.sleep.dto.SaveVideoRequestDto;
import com.nosleepdrive.nosleepdrivebackend.sleep.dto.SleepListParamDto;
import com.nosleepdrive.nosleepdrivebackend.sleep.repository.SleepRepository;
import com.nosleepdrive.nosleepdrivebackend.sleep.repository.entity.Sleep;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SleepService {

    private final SleepRepository sleepRepository;
    @Value("${upload.dir}")
    private String uploadDir;

    @Transactional
    public void saveSleepData(Driver curDriver, SaveVideoRequestDto body){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String dateValue = sdf.format(body.getDetectedAtDate())+"/";
        File directory = new File(uploadDir+dateValue);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            String originalFilename = body.getVideoFile().getOriginalFilename();
            String totalPath = uploadDir +dateValue+ originalFilename;
            String baseName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            File destinationFile = new File(totalPath);
            int count = 1;
            while (destinationFile.exists()) {
                String newFileName = baseName + "_" + count + extension;
                totalPath = uploadDir + dateValue + newFileName;
                destinationFile = new File(totalPath);
                count++;
            }

            body.getVideoFile().transferTo(destinationFile);

            Sleep sleep = Sleep.builder()
                    .sleepTime(body.getDetectedAtDate())
                    .sleepVideoPath(totalPath)
                    .driver(curDriver)
                    .build();
            sleepRepository.save(sleep);
        }
        catch (CustomError e) {
            throw e;
        }
        catch (Exception e) {
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_INVALID_VIDEO.getMessage());
        }
    }

    public int getTodaySleepCount(Company curCompany){
        return sleepRepository.getCountSleepsByCompanyIdAndDate(curCompany.getIdCompany(), new Date());
    }

    public List<Sleep> getRecentSleeps(Company curCompany){
        return sleepRepository.getRecentSleep(curCompany.getIdCompany());
    }

    public List<Sleep> getSleepList(Company curCompany, SleepListParamDto data){
        return sleepRepository.getFilteredSleepData(
                curCompany.getIdCompany(),
                data.getStartDate(),
                data.getEndDate(),
                data.getVehicleNumber(),
                data.getDriverHash());
    }

    public Sleep getSleep(Company curCompany, long sleepId) {
        Sleep sleep = sleepRepository.findByIdSleep(sleepId);
        if(sleep==null) {
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_NOT_FOUND_VEHICLE.getMessage());
        }
        if(sleep.getDriver().getVehicle().getCompany() != curCompany){
            throw new CustomError(HttpStatus.FORBIDDEN.value(), Message.ERR_FORBIDDEN.getMessage());
        }
        return sleep;
    }

    public File getSleepVideoFile(Company curCompany, long sleepId) {
        Sleep sleep = sleepRepository.findByIdSleep(sleepId);
        if(sleep==null) {
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_NOT_FOUND_VEHICLE.getMessage());
        }
        if(sleep.getDriver().getVehicle().getCompany() != curCompany){
            throw new CustomError(HttpStatus.FORBIDDEN.value(), Message.ERR_FORBIDDEN.getMessage());
        }
        File videoFile = new File(sleep.getSleepVideoPath());
        if (!videoFile.exists()) {
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_INVALID_VIDEO_PATH.getMessage());
        }
        return videoFile;
    }

    public List<File> getSleepVideoFiles(Company curCompany, List<Long> Ids) {
        List<File> result = new LinkedList<>();
        List<Sleep> sleeps = sleepRepository.getSleepsByIdSleepIsIn(Ids);
        for(Sleep sleep: sleeps){
            if(sleep==null) {
                continue;
            }
            if(sleep.getDriver().getVehicle().getCompany() != curCompany){
                continue;
            }
            File videoFile = new File(sleep.getSleepVideoPath());
            if (!videoFile.exists()) {
                continue;
            }
            result.add(videoFile);
        }

        return result;
    }
}
