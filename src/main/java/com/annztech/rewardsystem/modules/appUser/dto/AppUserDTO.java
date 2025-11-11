package com.annztech.rewardsystem.modules.appUser.dto;

import com.annztech.rewardsystem.modules.appUser.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    String id;
    String email;
    String userType;
    boolean isActive;
    String status;

    public AppUserDTO(AppUser appUser) {
        this.id = appUser.getId().toString();
        this.email = appUser.getEmail();
        this.isActive = appUser.getIsActive();
        this.status = appUser.getStatus();
    }
}
