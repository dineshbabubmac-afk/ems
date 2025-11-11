package com.annztech.rewardsystem.modules.appUser.service;

import com.annztech.rewardsystem.modules.appUser.dto.AppUserCreateDTO;
import com.annztech.rewardsystem.modules.appUser.dto.AppUserDTO;
import com.annztech.rewardsystem.modules.appUser.dto.AppUserIdResponseDTO;
import com.annztech.rewardsystem.modules.appUser.entity.AppUser;

import java.util.List;
import java.util.Optional;

public interface AppUserService {
    List<AppUserDTO> getAllAppUsersDTOs();
    AppUserDTO getAppUserDTOById(String id);
    AppUserIdResponseDTO createAppUser(AppUserCreateDTO appUser);
    AppUser findUserByEmail(String email);
    void deactiveAppUser(String userId);
    void activeAppUser(String userId);

    AppUser getAppUserEntityEmailAndAppCode(String email, String appCode);
}
