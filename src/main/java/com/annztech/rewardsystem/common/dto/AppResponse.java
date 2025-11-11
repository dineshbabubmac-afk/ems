package com.annztech.rewardsystem.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AppResponse {
    private Object data;
    private String status;
    private String message;
    private AppError error;
    private String timestamp;

    public AppResponse(String status, String message, Object data, AppError error) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
        this.timestamp = Instant.now().toString();
    }

    public static ResponseEntity<Object> success(String message, HttpStatus status, Object data) {
        AppResponse appResponse = new AppResponse("success", message, data, null);
        return ResponseEntity.status(status).body(appResponse);
    }

    public static ResponseEntity<Object> error(HttpStatus status, String errorMessage) {
        AppError appError = new AppError(status.value(), errorMessage);
        AppResponse appResponse = new AppResponse("error", status.getReasonPhrase(), null, appError);
        return ResponseEntity.status(status).body(appResponse);
    }
}
