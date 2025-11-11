package com.annztech.rewardsystem.modules.appUser.dto;

import com.annztech.rewardsystem.modules.appUser.entity.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
public class AppUserIdResponseDTO {
    private String id;
    private String userCode;

    public AppUserIdResponseDTO(AppUser appUser) {
        this.id = appUser.getId().toString();
        this.userCode = appUser.getUserCode();
    }
}
