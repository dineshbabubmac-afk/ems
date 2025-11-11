package com.annztech.rewardsystem.modules.appUser.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUserIdUpdateEvent {
    private UUID userId;
    private String email;
}
