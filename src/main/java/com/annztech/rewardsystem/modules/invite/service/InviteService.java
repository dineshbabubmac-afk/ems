package com.annztech.rewardsystem.modules.invite.service;

import com.annztech.rewardsystem.modules.invite.dto.InviteDTO;
import com.annztech.rewardsystem.modules.invite.dto.InviteUpdateDTO;
import com.annztech.rewardsystem.modules.invite.entity.Invite;

import java.util.List;

public interface InviteService {
    InviteDTO getInviteDTOById(String id);
    Invite getInviteEntityById(String id);
    InviteDTO updateInviteStatus(String id, InviteUpdateDTO dto);
    InviteDTO resendUpdateStatus(String id);
    List<InviteDTO> getAllInviteDTO();
    InviteDTO deleteInvite(String id);
}
