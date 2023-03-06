package com.etmoney.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealtcheckController {

    @GetMapping("/ping")
    public String health() {
        return "pong";
    }

    @GetMapping("/isActive")
    public String isActive() {
        return "ACTIVE";
    }

}
