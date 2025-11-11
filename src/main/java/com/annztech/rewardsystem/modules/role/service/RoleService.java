package com.annztech.rewardsystem.modules.role.service;

import com.annztech.rewardsystem.modules.role.dto.RoleDTO;
import com.annztech.rewardsystem.modules.role.entity.Role;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAlRolesDTO();

    List<RoleDTO> getClientRolesDTO();

    Role getEmployeeSupportRole();

    Role getClientSupportRole();

    Role getRoleEntityById(String id);

    Role getRoleEntityByName(String name);
}
