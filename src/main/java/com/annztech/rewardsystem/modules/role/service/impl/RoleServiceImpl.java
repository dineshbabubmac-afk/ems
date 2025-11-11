package com.annztech.rewardsystem.modules.role.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.ValidationUtils;
import com.annztech.rewardsystem.modules.role.constants.RoleConstants;
import com.annztech.rewardsystem.modules.role.dto.RoleDTO;
import com.annztech.rewardsystem.modules.role.entity.Role;
import com.annztech.rewardsystem.modules.role.repository.RoleRepository;
import com.annztech.rewardsystem.modules.role.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl extends LocalizationService implements RoleService {
    private final RoleRepository roleRepository;
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDTO> getAlRolesDTO() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(RoleDTO::new).toList();
    }

    @Override
    public  List<RoleDTO> getClientRolesDTO() {
        List<Role> roles = roleRepository.getRoleByScope("CLIENT");
        return roles.stream().map(RoleDTO::new).toList();
    }

    @Override
    public Role getEmployeeSupportRole() {
        return roleRepository.findEmpSupportId().orElseThrow(() -> new AppException(getMessage(RoleConstants.ROLE_NOT_FOUND), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public Role getClientSupportRole() {
        return roleRepository.findClientSupportId().orElseThrow(() -> new AppException(getMessage(RoleConstants.ROLE_NOT_FOUND), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public Role getRoleEntityById(String id) {
        if(StringUtils.isBlank(id)){
            throw new AppException(getMessage(RoleConstants.ROLE_ID_EMPTY), HttpStatus.BAD_REQUEST);
        }
        if(!ValidationUtils.isValidUUID(id)){
            throw new AppException(getMessage(RoleConstants.ROLE_ID_INVALID), HttpStatus.BAD_REQUEST);
        }
        return roleRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(getMessage(RoleConstants.ROLE_NOT_FOUND), HttpStatus.BAD_REQUEST));
    }

    @Override
    public Role getRoleEntityByName(String name) {
        if(StringUtils.isBlank(name)){
            throw new AppException(getMessage(RoleConstants.ROLE_ID_EMPTY), HttpStatus.BAD_REQUEST);
        }
        return roleRepository.getRoleByCode(name).orElseThrow(() -> new AppException(getMessage(RoleConstants.ROLE_NAME_INVALID), HttpStatus.BAD_REQUEST));
    }
}
