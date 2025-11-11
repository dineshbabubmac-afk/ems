package com.annztech.rewardsystem.common.exception;


import com.annztech.rewardsystem.common.constants.AppConstant;
import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends LocalizationService {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(AppException ex) {
        return AppResponse.error(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAppAuthException(AppException ex) {
        return AppResponse.error(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException e) {
        logger.error(e.getMessage(), e);
        return AppResponse.error(HttpStatus.BAD_REQUEST, getMessage("User not found!"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return AppResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException e) {
        logger.error(e.getMessage(), e);
        return AppResponse.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error(e.getMessage(), e);
        return AppResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtException(JwtException e) {
        logger.error(e.getMessage(), e);
        return AppResponse.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        String message = "Duplicate entry detected!";
        HttpStatus status = HttpStatus.CONFLICT;

        // Check for the specific cause if available
        switch (ex) {
            case DataIntegrityViolationException dataEx -> {

                if (dataEx.getRootCause() != null) {
                    String rootMessage = dataEx.getRootCause().getMessage();
                    logger.error("DataIntegrityViolationException root cause: {}", rootMessage);
                    // Handle duplicate key violations
                    if (rootMessage.contains("duplicate key value violates unique constraint")) {
                        message = AppConstant.DUPLICATE_ENTRY;
                    } else {
                        // If not a duplicate key error, return a generic error message
                        message = AppConstant.INSERT_ERROR;
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                    }
                }
            }
            case ConstraintViolationException constraintViolationException -> {
                // Specific handling for ConstraintViolationException if needed
                message = AppConstant.CONSTRAINT_ERROR;
                status = HttpStatus.BAD_REQUEST;
            }
            case SQLException throwables -> {
                // Specific handling for SQLException
                message = AppConstant.DB_ERROR;
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            default -> {
                message = AppConstant.GENRIC_ERROR_MSG;
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        // Return custom error response
        return AppResponse.error(status, getMessage(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return AppResponse.error(HttpStatus.BAD_REQUEST, getMessage( String.valueOf(errorMessages)));
    }
}
