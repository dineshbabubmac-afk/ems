package com.annztech.rewardsystem.common.utils;

import com.annztech.rewardsystem.common.exception.AppException;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Condition;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;

public class StringsUtils {
    public static String capitalizeName(String name) {
        if (name == null || name.isEmpty()) return name;
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public static void validateUUID(String id, String errorMessage) {
        if (StringUtils.isBlank(id) || !ValidationUtils.isValidUUID(id)) {
            throw new AppException(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @Named("isNotBlank")
    @Condition
    public static boolean isNotBlank(String value) {
        return StringUtils.isNotBlank(value);
    }
}
