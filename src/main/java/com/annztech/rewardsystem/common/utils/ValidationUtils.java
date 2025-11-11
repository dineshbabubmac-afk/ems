package com.annztech.rewardsystem.common.utils;

import com.annztech.rewardsystem.common.exception.AppException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class ValidationUtils {
    private ValidationUtils() {}

    public static boolean isValidUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (Exception e) {
            throw new AppException("Invalid Id", HttpStatus.BAD_REQUEST);
        }
    }

    public static boolean isValidId(Long value) {
        return value != null && value != 0L;
    }

    public static boolean isValidCost(BigDecimal cost) {
       return cost != null && cost.compareTo(BigDecimal.ZERO) > 0;
    }
}
