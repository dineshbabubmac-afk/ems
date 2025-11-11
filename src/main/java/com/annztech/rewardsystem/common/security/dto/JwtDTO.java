package com.annztech.rewardsystem.common.security.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtDTO {
    private String id;
    private String email;
    private String role;
    private String scope;
    private String tokenType;
    private String appCode;
}
