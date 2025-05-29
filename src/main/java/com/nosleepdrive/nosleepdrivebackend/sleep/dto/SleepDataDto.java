package com.nosleepdrive.nosleepdrivebackend.sleep.dto;

import com.nosleepdrive.nosleepdrivebackend.sleep.repository.entity.Sleep;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
@NoArgsConstructor
public class SleepDataDto {
    @NonNull
    private Long idSleep;

    @NonNull
    private String vehicleNumber;

    @NonNull
    private Date detectedTime;

    @NonNull
    private String driverHash;

    public static SleepDataDto of(Sleep sleep) {
        SleepDataDto dto = new SleepDataDto();
        dto.idSleep = sleep.getIdSleep();
        dto.vehicleNumber = sleep.getDriver().getVehicle().getCarNumber();
        dto.detectedTime = SleepDataDto.parseToKST(sleep.getSleepTime());
        dto.driverHash = sleep.getDriver().getDriverHash();
        return dto;
    }

    private static Date parseToKST(String datetime) {
        try{
            String trimmed = datetime;
            if (datetime.contains(".")) {
                int dotIndex = datetime.indexOf('.');
                String millis = datetime.substring(dotIndex + 1);
                millis = millis + "000000"; // 부족한 자리 0으로 채움
                trimmed = datetime.substring(0, dotIndex + 1) + millis.substring(0, 3);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // KST 설정
            return dateFormat.parse(trimmed);
        }
        catch(ParseException e){
            return null;
        }
    }
}
