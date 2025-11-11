package com.annztech.rewardsystem.modules.role.dto;

import com.annztech.rewardsystem.modules.role.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    String id;
    String roleName;
    String roleCode;
    String scope;

    public RoleDTO(Role role) {
        this.id = role.getId().toString();
        this.roleName = role.getName();
        this.scope = role.getScope();
        this.roleCode = role.getCode();
    }
}
