package com.annztech.rewardsystem.external.email.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zepto")
@Getter
@Setter
public class EmailConfig {
    private String apiKey;
    private String baseUrl;
}
