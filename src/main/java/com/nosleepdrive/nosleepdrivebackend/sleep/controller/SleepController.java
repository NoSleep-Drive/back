package com.nosleepdrive.nosleepdrivebackend.sleep.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class SleepController {
    @GetMapping("/sleep")
    public String hello() {
        return "Hello, NoSleepDrive!\n it's a sleep!";
    }
}
