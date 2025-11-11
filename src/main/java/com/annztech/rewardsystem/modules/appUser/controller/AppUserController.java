package com.annztech.rewardsystem.modules.appUser.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.appUser.constants.AppUserConstants;
import com.annztech.rewardsystem.modules.appUser.dto.AppUserCreateDTO;
import com.annztech.rewardsystem.modules.appUser.service.AppUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "App User", description = "Application user register, login and lookup")
public class AppUserController extends LocalizationService {
    private final AppUserService appUserService;
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllAppUsers() {
        return AppResponse.success(getMessage(AppUserConstants.USER_FETCHED), HttpStatus.OK, appUserService.getAllAppUsersDTOs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAppUserById(@PathVariable String id) {
        return AppResponse.success(getMessage(AppUserConstants.USER_FETCHED), HttpStatus.OK, appUserService.getAppUserDTOById(id));
    }

    @PostMapping
    public ResponseEntity<Object> createAppUser( @RequestBody @Valid AppUserCreateDTO  appUserCreateDTO) {
        return AppResponse.success(getMessage(AppUserConstants.USER_CREATED), HttpStatus.CREATED, appUserService.createAppUser(appUserCreateDTO));
    }
}
