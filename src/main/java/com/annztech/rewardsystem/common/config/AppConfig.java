package com.annztech.rewardsystem.common.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @PostConstruct
    public void logDbUrl() {
        System.out.println("Using DB URL: " + dbUrl);
    }
}
