package com.annztech.rewardsystem.external.config;

import com.annztech.rewardsystem.external.email.config.EmailConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final EmailConfig emailConfig;

    public WebClientConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    @Bean
    @Qualifier("zeptoEmailClient")
    public WebClient ZeptoEmailClient() {
        return WebClient.builder()
                .baseUrl(emailConfig.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, emailConfig.getApiKey())
                .build();
    }

}
