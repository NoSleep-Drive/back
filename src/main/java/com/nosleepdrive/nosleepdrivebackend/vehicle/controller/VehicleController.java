package com.nosleepdrive.nosleepdrivebackend.vehicle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleController {
    @GetMapping("/vehicle")
    public String hello() {
        return "Hello, NoSleepDrive!\n it's a vehicle!";
    }
}
