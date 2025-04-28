package com.nosleepdrive.nosleepdrivebackend.company.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class CompanyController {
    @GetMapping("/company")
    public String hello() {
        return "Hello, NoSleepDrive! it's a company!";
    }
}
