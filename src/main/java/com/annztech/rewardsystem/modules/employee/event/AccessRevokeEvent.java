package com.annztech.rewardsystem.modules.employee.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessRevokeEvent {
    String email;
    String appType;
}
