package com.annztech.rewardsystem.common.security.handler;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

//The purpose of the class to show the genric response structure across the app
@Component
public class AppAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper;

    public AppAccessDeniedHandler() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("status", "error");
        errorData.put("message", "Access Denied");
        errorData.put("error", Map.of(
                "code", 403,
                "message", accessDeniedException.getMessage()
        ));
        errorData.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC));
        mapper.writeValue(response.getWriter(), errorData);
    }
}
