package com.annztech.rewardsystem.modules.common.helper;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.security.dto.JwtDTO;
import com.annztech.rewardsystem.modules.common.constants.DomainConstants;
import com.annztech.rewardsystem.modules.common.enums.DomainRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class DomainHelper {
    public static Sort sortByUpdatedAtDesc() {
        return Sort.by(Sort.Direction.DESC, "updatedAt");
    }

    public static Sort sortByCreatedAtDesc() {
        return Sort.by(Sort.Direction.DESC, "createdAt");
    }

    public static String getId() {
        JwtDTO jwtDTO = getJwtDTO();
        return jwtDTO.getId();
    }

    public static boolean checkSuperAdmin() {
        JwtDTO jwtDTO = getJwtDTO();
        return jwtDTO.getRole().equals(DomainRoles.SUPER_ADMIN.name());
    }

    public static String getUserEmail() {
        JwtDTO jwtDTO = getJwtDTO();
        return jwtDTO.getEmail();
    }

    public static JwtDTO getJwtDTO() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtDTO jwtDTO = (JwtDTO) authentication.getPrincipal();
        if (jwtDTO == null) {
            throw new AppException("Invalid token", HttpStatus.UNAUTHORIZED);
        }
        return jwtDTO;
    }

    public static boolean hasFromAdminApp() {
        JwtDTO jwtDTO = getJwtDTO();
        return jwtDTO.getAppCode().equalsIgnoreCase(DomainConstants.EMPLOYEE_ADMIN_APP);
    }
}
