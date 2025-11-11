package com.annztech.rewardsystem.modules.auth.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.security.config.JwtConfig;
import com.annztech.rewardsystem.common.security.dto.JwtDTO;
import com.annztech.rewardsystem.common.security.helper.JwtHelper;
import com.annztech.rewardsystem.modules.appUser.constants.AppUserConstants;
import com.annztech.rewardsystem.modules.appUser.entity.AppUser;
import com.annztech.rewardsystem.modules.appUser.repository.AppUserRepository;
import com.annztech.rewardsystem.modules.auth.constants.AuthConstants;
import com.annztech.rewardsystem.modules.auth.dto.AuthLoginDTO;
import com.annztech.rewardsystem.modules.auth.dto.AuthResponseDTO;
import com.annztech.rewardsystem.modules.auth.service.AuthService;
import com.annztech.rewardsystem.modules.common.constants.DomainConstants;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeDTO;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.employee.repository.EmployeeRepository;
import io.jsonwebtoken.Claims;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;


@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final EmployeeRepository employeeRepository;
    private final JwtConfig jwtConfig;

    public AuthServiceImpl(AppUserRepository appUserRepository, EmployeeRepository employeeRepository, JwtConfig jwtConfig) {
        this.appUserRepository = appUserRepository;
        this.employeeRepository = employeeRepository;
        this.jwtConfig = jwtConfig;
    }


    @Override
    public UserDetails loadUserByUsername(String email)  {
        AppUser appUser = appUserRepository.findAppUsersByEmail(email).orElseThrow(() -> new AppException(AuthConstants.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED));
        if (!appUser.getIsActive()) {
            throw new AppException(AuthConstants.ACCOUNT_DEACTIVATED, HttpStatus.FORBIDDEN);
        }
        if(appUser.getStatus().equals(AppUserConstants.BLOCKED)){
            throw new AppException(AuthConstants.ACCOUNT_DEACTIVATED, HttpStatus.FORBIDDEN);
        }
        return new User(appUser.getEmail(), appUser.getPassword(), Collections.emptyList());
    }

    @Override
    public Pair<AuthResponseDTO, String> getAccessToken(AuthLoginDTO authLoginDTO) {
        Employee employee = employeeRepository.findEmployeesByEmail(authLoginDTO.getEmail());
        AppUser appUser = appUserRepository.findAppUsersByEmail(authLoginDTO.getEmail()).orElseThrow(() -> new AppException(AuthConstants.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED));
        if (employee == null) {
            throw new AppException(AuthConstants.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
        }
        updateLastLoginAt(employee);
        JwtDTO access_jwtDto = getJwtDTO(employee, "access_token", appUser.getAppCode());
        JwtDTO refersh_jwtDto = getJwtDTO(employee, "refresh_token", appUser.getAppCode());
        String accessToken = JwtHelper.generateToken(access_jwtDto, jwtConfig.getAccessTokenExpire(), jwtConfig.getSecretKey());
        String refreshToken = JwtHelper.generateToken(refersh_jwtDto, jwtConfig.getRefreshTokenExpire(), jwtConfig.getSecretKey());
        AuthResponseDTO dto = new AuthResponseDTO(accessToken, jwtConfig.getAccessTokenExpire());
        return new Pair<>(dto, refreshToken);
    }

    @Override
    public Pair<AuthResponseDTO, String> renewRefreshToken(String token) {
        if(token == null || token.isBlank()) {
            throw new AppException(AuthConstants.REFRESH_TOKEN_EMPTY, HttpStatus.UNAUTHORIZED);
        }
        Claims claims = JwtHelper.getClaims(token, jwtConfig.getSecretKey());
        String id = claims.getSubject();
//        if (!ValidationUtils.isValidUUID(token)) {
//            throw new AppException("Invalid token", HttpStatus.UNAUTHORIZED);
//        }
        UUID employeeId = UUID.fromString(id);
        Employee employee = employeeRepository.findEmployeeById(employeeId);
        AppUser appUser = appUserRepository.findAppUsersByEmail(employee.getEmail()).orElseThrow(() -> new AppException(AuthConstants.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED));
        if (employee == null) {
            throw new AppException(AuthConstants.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
        }
        updateLastLoginAt(employee);
        JwtDTO access_jwtDto = getJwtDTO(employee, "access_token", appUser.getAppCode());
        JwtDTO refersh_jwtDto = getJwtDTO(employee, "refresh_token", appUser.getAppCode());
        String accessToken = JwtHelper.generateToken(access_jwtDto, jwtConfig.getAccessTokenExpire(), jwtConfig.getSecretKey());
        String refreshToken = JwtHelper.generateToken(refersh_jwtDto, jwtConfig.getRefreshTokenExpire(), jwtConfig.getSecretKey() );
        AuthResponseDTO dto = new AuthResponseDTO(accessToken, jwtConfig.getAccessTokenExpire());
        return new Pair<>(dto, refreshToken);
    }

    @Override
    public EmployeeDTO getEmployeeDTO() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtDTO jwtDTO = (JwtDTO) authentication.getPrincipal();
        if (jwtDTO == null) {
            throw new AppException(AuthConstants.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
        }
        UUID employeeId = UUID.fromString(jwtDTO.getId());
        Employee employee = employeeRepository.findEmployeeById(employeeId);
        return new EmployeeDTO(employee);
    }

    private JwtDTO getJwtDTO(Employee employee, String type, String appCode) {
        return JwtDTO.builder()
                .scope(employee.getRole().getScope())
                .id(employee.getId().toString())
                .email(employee.getEmail())
                .role(employee.getRole().getCode())
                .appCode(appCode)
                .tokenType(type).build();
    }

    private void updateLastLoginAt(Employee employee) {
        employee.setLastLoginAt(Instant.now());
        employeeRepository.save(employee);
    }
}
