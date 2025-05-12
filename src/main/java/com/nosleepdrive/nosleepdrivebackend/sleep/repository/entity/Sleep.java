package com.nosleepdrive.nosleepdrivebackend.sleep.repository.entity;

import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Sleep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sleep", updatable = false)
    private Long idSleep;

    @NonNull
    @Column(name = "sleep_video_path", updatable = false, nullable = false, length = 4096)
    private String sleepVideoPath;

    @NonNull
    @Column(name = "sleep_time", updatable = false, nullable = false)
    private Date sleepTime;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_driver", referencedColumnName = "id_driver", nullable = false)
    private Driver driver;
}
