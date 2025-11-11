package com.annztech.rewardsystem.modules.invite.dto;

import com.annztech.rewardsystem.modules.invite.event.AdminInviteEmailEvent;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkAdminInviteEmailEvent {
    private List<AdminInviteEmailEvent> emails;
}
