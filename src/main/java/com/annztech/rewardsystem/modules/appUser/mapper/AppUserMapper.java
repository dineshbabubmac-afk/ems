package com.annztech.rewardsystem.modules.appUser.mapper;

import com.annztech.rewardsystem.modules.appUser.dto.AppUserCreateDTO;
import com.annztech.rewardsystem.modules.appUser.dto.AppUserDTO;
import com.annztech.rewardsystem.modules.appUser.dto.AppUserIdResponseDTO;
import com.annztech.rewardsystem.modules.appUser.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserDTO toDto(AppUser appUser);
    @Mapping(target = "id", ignore = true)
    AppUser toCreateNewEntity(AppUserCreateDTO appUserDTO);
}
