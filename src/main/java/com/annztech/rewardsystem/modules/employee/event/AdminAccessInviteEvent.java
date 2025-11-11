package com.annztech.rewardsystem.modules.employee.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminAccessInviteEvent {
    private String senderEmail;
    private String receiverEmail;
    private String referenceId;
    private String departmentName;
    private String appType;
    private String name;
}
