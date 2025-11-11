package com.annztech.rewardsystem.modules.invite.dto;

import com.annztech.rewardsystem.modules.invite.entity.Invite;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InviteDTO {
    String id;
    String invitedBy;
    String invitedTo;
    String receiverEmail;
    String referenceId;
    boolean isExpired;
    String path;
    String status;
    String appCode;

    public InviteDTO(Invite invite) {
        this.id = invite.getId().toString();
        this.receiverEmail = invite.getReceiverEmail();
        this.referenceId = invite.getReferenceId().toString();
        this.isExpired = Instant.now().isAfter(invite.getExpiresAt());
        this.status = invite.getStatus();
        this.appCode = invite.getAppType();
    }
}
