package com.nosleepdrive.nosleepdrivebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.nosleepdrive.nosleepdrivebackend")
public class NosleepdriveBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NosleepdriveBackendApplication.class, args);
    }

}
