package com.annztech.rewardsystem.modules.invite.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInviteEmailEvent {
    public String toEmail;
    public String fromEmail;
    public String subject;
    public String link;
    public String inviterName;
    public String inviteeName;
}
