package com.nosleepdrive.nosleepdrivebackend.driver.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverController {
    @GetMapping("/driver")
    public String hello() {
        return "Hello, NoSleepDrive!\n it's a driver!";
    }
}
