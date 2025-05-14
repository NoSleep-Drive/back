package com.nosleepdrive.nosleepdrivebackend.sleep.dto;

import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SaveVideoRequestDto {
    @NonNull
    private String deviceUid;

    @NonNull
    private String detectedAt;

    private Date detectedAtDate;

    private String checksum;

    @NonNull
    private MultipartFile videoFile;
}
