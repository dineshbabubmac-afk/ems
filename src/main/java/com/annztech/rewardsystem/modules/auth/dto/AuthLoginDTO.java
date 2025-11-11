package com.annztech.rewardsystem.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginDTO {
    @NotBlank(message = "{auth.login.email.required}")
    @Email
    String email;
    @NotBlank(message = "{auth.login.password.required}")
    String password;
}
