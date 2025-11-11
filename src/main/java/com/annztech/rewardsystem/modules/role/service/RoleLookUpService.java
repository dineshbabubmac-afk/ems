package com.annztech.rewardsystem.modules.role.service;

import com.annztech.rewardsystem.modules.role.entity.Role;

import java.util.List;

public interface RoleLookUpService {
    List<Role> getCode(List<String> roleCodes);
}
