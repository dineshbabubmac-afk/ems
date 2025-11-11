package com.annztech.rewardsystem.modules.appUser.service;

import com.annztech.rewardsystem.modules.invite.entity.Invite;


public interface AppUserCompositeService {
    Invite getInviteById(String id);
}
