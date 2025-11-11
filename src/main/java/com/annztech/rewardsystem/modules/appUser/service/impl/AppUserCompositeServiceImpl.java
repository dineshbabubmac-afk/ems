package com.annztech.rewardsystem.modules.appUser.service.impl;

import com.annztech.rewardsystem.modules.appUser.service.AppUserCompositeService;
import com.annztech.rewardsystem.modules.invite.entity.Invite;
import com.annztech.rewardsystem.modules.invite.service.InviteService;
import org.springframework.stereotype.Service;

@Service
public class AppUserCompositeServiceImpl implements AppUserCompositeService {
    private final InviteService inviteService;
    public AppUserCompositeServiceImpl(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @Override
    public Invite getInviteById(String id) {
        return inviteService.getInviteEntityById(id);
    }

}
