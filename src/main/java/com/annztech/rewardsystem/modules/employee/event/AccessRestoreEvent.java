package com.annztech.rewardsystem.modules.employee.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessRestoreEvent {
    String email;
    String appType;
}
