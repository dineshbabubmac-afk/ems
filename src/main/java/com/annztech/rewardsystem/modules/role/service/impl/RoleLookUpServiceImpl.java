package com.annztech.rewardsystem.modules.role.service.impl;

import com.annztech.rewardsystem.modules.role.entity.Role;
import com.annztech.rewardsystem.modules.role.repository.RoleRepository;
import com.annztech.rewardsystem.modules.role.service.RoleLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleLookUpServiceImpl implements RoleLookUpService {

    private final RoleRepository roleRepository;

    public RoleLookUpServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getCode(List<String> roleCodes) {
        log.info("Fetching roles related to ADMIN/SUPER_ADMIN...");
        return roleRepository.findByCodeInIgnoreCase(roleCodes);
    }

}
