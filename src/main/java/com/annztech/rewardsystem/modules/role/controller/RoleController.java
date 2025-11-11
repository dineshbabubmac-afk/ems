package com.annztech.rewardsystem.modules.role.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.role.constants.RoleConstants;
import com.annztech.rewardsystem.modules.role.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/internal/roles")
@RestController
@Tag(name = "Internal Role Access Service", description = "Handles Role Access Service management for internal members")
public class RoleController extends LocalizationService {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllRoles() {
        return AppResponse.success(getMessage(RoleConstants.ROLES), HttpStatus.OK, roleService.getAlRolesDTO());
    }
}
