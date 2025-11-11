package com.annztech.rewardsystem;

import com.annztech.rewardsystem.external.email.config.EmailConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@EnableConfigurationProperties({EmailConfig.class})
public class RewardSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewardSystemApplication.class, args);
    }

}
