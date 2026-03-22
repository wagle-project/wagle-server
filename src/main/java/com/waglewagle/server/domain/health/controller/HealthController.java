package com.waglewagle.server.domain.health.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthControllerDocs {

    @GetMapping("/health")
    @Override
    public String healthCheck() {
        THIS_IS_A_DELIBERATE_COMPILE_ERROR
        return "I'm alive!!!";
    }
}