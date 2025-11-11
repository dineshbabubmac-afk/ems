package com.annztech.rewardsystem.modules.appUser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserCreateDTO {
    @NotBlank(message = "{appUser.email.required}")
    @Email
    private String email;
    @NotBlank(message = "{appUser.password.required}")
    private String password;
    @NotBlank(message = "{appUser.inviteId.required}")
    private String inviteId;
}
