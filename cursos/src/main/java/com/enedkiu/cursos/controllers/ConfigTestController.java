package com.enedkiu.cursos.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigTestController {

    @Value("${app.api.key}")
    private String apiKey;

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/config/test")
    public String testConfig() {
        return String.format(
            "App: %s | API Key: %s | Longitud: %d",
            appName,
            apiKey,
            apiKey.length()
        );
    }
}