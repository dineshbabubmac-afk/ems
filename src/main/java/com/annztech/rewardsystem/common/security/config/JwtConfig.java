package com.annztech.rewardsystem.common.security.config;

import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtConfig {
    private String secret;
    private Long accessTokenExpire;
    private Long refreshTokenExpire;

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
