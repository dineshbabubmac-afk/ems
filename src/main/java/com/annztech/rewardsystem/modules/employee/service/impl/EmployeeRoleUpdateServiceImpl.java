package com.annztech.rewardsystem.modules.employee.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.security.dto.JwtDTO;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.appUser.entity.AppUser;
import com.annztech.rewardsystem.modules.appUser.service.AppUserService;
import com.annztech.rewardsystem.modules.common.constants.DomainConstants;
import com.annztech.rewardsystem.modules.employee.constants.EmployeeConstants;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeAccessRoleDTO;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeIdResponseDTO;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeUpdateUserAccessDTO;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.employee.event.AccessRestoreEvent;
import com.annztech.rewardsystem.modules.employee.event.AccessRevokeEvent;
import com.annztech.rewardsystem.modules.employee.event.AdminAccessInviteEvent;
import com.annztech.rewardsystem.modules.employee.repository.EmployeeRepository;
import com.annztech.rewardsystem.modules.employee.service.EmployeeRoleUpdateService;
import com.annztech.rewardsystem.modules.invite.dto.BulkAdminAccessInviteEvent;
import com.annztech.rewardsystem.modules.role.entity.Role;
import com.annztech.rewardsystem.modules.role.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EmployeeRoleUpdateServiceImpl extends LocalizationService implements EmployeeRoleUpdateService {
    private final AppUserService appUserService;
    private final RoleService roleService;
    private final EmployeeRepository employeeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public EmployeeRoleUpdateServiceImpl(AppUserService appUserService, RoleService roleService,
                                         EmployeeRepository employeeRepository, ApplicationEventPublisher eventPublisher) {
        this.appUserService = appUserService;
        this.roleService = roleService;
        this.employeeRepository = employeeRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public EmployeeIdResponseDTO updateEmployeeRole(EmployeeUpdateUserAccessDTO dto) {
        ArrayList<AdminAccessInviteEvent> adminAccessInviteList = new ArrayList<>();
        ArrayList<AccessRevokeEvent> accessRevokeEventList = new ArrayList<>();
        ArrayList<AccessRestoreEvent> accessRestoreEvents = new ArrayList<>();
        JwtDTO jwtDTO = getJwtDTOFromSecurityContext();
        for (EmployeeAccessRoleDTO roleDTO : dto.getUpdateRoles()) {
            Employee employee = employeeRepository.findById(roleDTO.getEmployeeId())
                    .orElseThrow(() -> new AppException(getMessage(EmployeeConstants.EMPLOYEE_NO_RECORDS), HttpStatus.NOT_FOUND));
            log.info("👨‍💼 Found employee: {} {} (current role: {})",
                    employee.getFirstName(), employee.getLastName(), employee.getRole().getCode());
            Role newRole = roleService.getRoleEntityById(String.valueOf(roleDTO.getRoleId()));
            log.info("🔄 New role: {}", newRole.getCode());
            processRoleUpdateEvents(newRole, employee, jwtDTO, adminAccessInviteList, accessRevokeEventList, accessRestoreEvents);
            updateEmployeeRoles(roleDTO.getEmployeeId(), employee, newRole);
        }
        publishAdminAccessEvent(adminAccessInviteList);
        publishAccessRevokeEvent(accessRevokeEventList);
        publishAccessRestoreEvent(accessRestoreEvents);
        return EmployeeIdResponseDTO.builder().message(EmployeeConstants.ROLE_UPDATED).build();
    }

    private void updateEmployeeRoles(UUID empId, Employee employeeEntity, Role newRole) {
        employeeEntity.setRole(newRole);
        employeeRepository.save(employeeEntity);
    }

    private JwtDTO getJwtDTOFromSecurityContext(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (JwtDTO) authentication.getPrincipal();
    }

    private void processRoleUpdateEvents(Role newRole, Employee employee, JwtDTO jwtDTO,
                                         ArrayList<AdminAccessInviteEvent> adminAccessInviteList,
                                         ArrayList<AccessRevokeEvent> accessRevokeEventList,
                                         ArrayList<AccessRestoreEvent> accessRestoreEvents) {
        if ((newRole.getCode().equals(EmployeeConstants.ROLE_ADMIN) ||
                newRole.getCode().equals(EmployeeConstants.ROLE_SUPER_ADMIN)) &&
                employee.getRole().getCode().equals(EmployeeConstants.ROLE_EMPLOYEE)) {
            AppUser appUser = appUserService.getAppUserEntityEmailAndAppCode(employee.getEmail(), DomainConstants.EMPLOYEE_ADMIN_APP);
            if (appUser == null) {
                AdminAccessInviteEvent event = AdminAccessInviteEvent.builder()
                        .senderEmail(jwtDTO.getEmail())
                        .receiverEmail(employee.getEmail())
                        .departmentName(employee.getDepartment().getNameEn())
                        .appType(DomainConstants.EMPLOYEE_ADMIN_APP)
                        .name(employee.getFirstName() + " " + employee.getLastName())
                        .referenceId(employee.getDepartment().getId().toString())
                        .build();
                adminAccessInviteList.add(event);
                log.info("✅ Added admin invite event for {}", employee.getEmail());
            } else {
                log.info("♻️ Existing admin access found - restoring for {}", employee.getEmail());
                accessRestoreEvents.add(new AccessRestoreEvent(employee.getEmail(), DomainConstants.EMPLOYEE_ADMIN_APP
                ));
            }
        }
        if (newRole.getCode().equals(EmployeeConstants.ROLE_EMPLOYEE) && employee.getRole().getCode().equals(EmployeeConstants.ROLE_ADMIN)) {
            AccessRevokeEvent accessRevokeEvent = AccessRevokeEvent.builder()
                    .email(employee.getEmail())
                    .appType(DomainConstants.EMPLOYEE_ADMIN_APP)
                    .build();
            accessRevokeEventList.add(accessRevokeEvent);
            log.info("✅ Added access revoke event for {}", employee.getEmail());
        }
    }

    private void publishAdminAccessEvent(List<AdminAccessInviteEvent> adminAccess){
        if(adminAccess == null || adminAccess.isEmpty()){
            return;
        }
        eventPublisher.publishEvent(new BulkAdminAccessInviteEvent(adminAccess));
        log.info("✅ Published BulkAdminAccessInviteEvent");
    }

    private void publishAccessRevokeEvent(List<AccessRevokeEvent> accessRevoke){
        if(accessRevoke == null || accessRevoke.isEmpty()){
            return;
        }
        log.info("🚀 Publishing {} access revoke events", accessRevoke.size());
        eventPublisher.publishEvent(accessRevoke);
    }

    private void publishAccessRestoreEvent(List<AccessRestoreEvent> accessRestore){
        if(accessRestore == null || accessRestore.isEmpty()){
            return;
        }
        log.info("🚀 Publishing {} access restore events", accessRestore.size());
        eventPublisher.publishEvent(accessRestore);
    }
}