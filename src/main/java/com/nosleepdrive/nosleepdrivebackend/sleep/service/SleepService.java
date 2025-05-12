package com.nosleepdrive.nosleepdrivebackend.sleep.service;

import com.nosleepdrive.nosleepdrivebackend.common.CustomError;
import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.common.SimpleResponse;
import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import com.nosleepdrive.nosleepdrivebackend.sleep.dto.SaveVideoRequestDto;
import com.nosleepdrive.nosleepdrivebackend.sleep.repository.SleepRepository;
import com.nosleepdrive.nosleepdrivebackend.sleep.repository.entity.Sleep;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
@RequiredArgsConstructor
public class SleepService {

    private final SleepRepository sleepRepository;
    @Value("${upload.dir}")
    private String uploadDir;

    @Transactional
    public void saveSleepData(Driver curDriver, SaveVideoRequestDto body){
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            String totalPath = uploadDir + body.getVideoFile().getOriginalFilename();
            File destinationFile = new File(totalPath);
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
            System.out.println(e.getMessage());
            throw new CustomError(HttpStatus.NOT_FOUND.value(), Message.ERR_INVALID_VIDEO.getMessage());
        }
    }
}
