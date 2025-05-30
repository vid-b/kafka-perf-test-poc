package com.sap.workflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    public static final String OK = "OK";

    @GetMapping("/health/ping")
    public String monitor() {
        return OK;
    }

}
