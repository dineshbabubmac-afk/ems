package com.annztech.rewardsystem.common.security.helper;

import com.annztech.rewardsystem.common.constants.AppConstant;
import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.security.dto.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtHelper {

    public static String generateToken(JwtDTO dto, long tokenExpiration, SecretKey key) {
        return Jwts.builder()
                .subject(dto.getId())
                .claim("email", dto.getEmail())
                .claim("role", dto.getRole())
                .claim("scope", dto.getScope())
                .claim("token_type", dto.getTokenType())
                .claim("appCode", dto.getAppCode())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + tokenExpiration))
                .signWith(key)
                .compact();
    }

    public static Claims getClaims(String token, SecretKey secretKey) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new AppException("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }

    public static boolean validateToken(String token, SecretKey secretKey) {
        var claims = JwtHelper.getClaims(token, secretKey);
        return claims.getExpiration().after(new Date());
    }

    public static JwtDTO getClaimsFromToken(String token, SecretKey secretKey) {
        var claims = JwtHelper.getClaims(token, secretKey);
        String id = claims.getSubject();
        String email = claims.get("email").toString();
        String role = claims.get("role").toString();
        String scope = claims.get("scope").toString();
        String tokenType = claims.get("token_type").toString();
        String appCode = claims.get("appCode").toString();
        return new JwtDTO(id, email, role, scope, tokenType, appCode);
    }

}
