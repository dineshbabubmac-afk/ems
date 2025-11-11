package com.annztech.rewardsystem.modules.invite.dto;

import com.annztech.rewardsystem.modules.employee.event.AdminAccessInviteEvent;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkAdminAccessInviteEvent {
    private List<AdminAccessInviteEvent> invites;
}
