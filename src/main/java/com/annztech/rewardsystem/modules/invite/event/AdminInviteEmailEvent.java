package com.annztech.rewardsystem.modules.invite.event;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminInviteEmailEvent {
    private String receiverEmail;
    private String senderEmail;
    private String departmentName;
    private String appType;
    private String name;
    private String inviteLink;
    private String subject;
}