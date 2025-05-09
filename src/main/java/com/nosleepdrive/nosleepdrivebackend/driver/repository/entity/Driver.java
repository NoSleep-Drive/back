package com.nosleepdrive.nosleepdrivebackend.driver.repository.entity;

import com.nosleepdrive.nosleepdrivebackend.sleep.repository.entity.Sleep;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_driver", updatable = false)
    private Long idDriver;

    @NonNull
    @Column(name = "start_time", nullable = false, updatable = false)
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehicle", referencedColumnName = "id_vehicle", nullable = false)
    private Vehicle vehicle;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Sleep> sleeps = new ArrayList<>();

    public void updateEndTime(Date newEndTime) {
        this.endTime = newEndTime;
    }
}
