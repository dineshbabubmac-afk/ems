package com.annztech.rewardsystem.modules.appUser.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.repsoitory.SequenceGeneratorRepository;
import com.annztech.rewardsystem.common.security.config.JwtConfig;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.common.utils.ValidationUtils;
import com.annztech.rewardsystem.modules.appUser.constants.AppUserConstants;
import com.annztech.rewardsystem.modules.appUser.dto.AppUserCreateDTO;
import com.annztech.rewardsystem.modules.appUser.dto.AppUserDTO;
import com.annztech.rewardsystem.modules.appUser.dto.AppUserIdResponseDTO;
import com.annztech.rewardsystem.modules.appUser.entity.AppUser;
import com.annztech.rewardsystem.modules.appUser.event.EmployeeUserIdUpdateEvent;
import com.annztech.rewardsystem.modules.appUser.mapper.AppUserMapper;
import com.annztech.rewardsystem.modules.appUser.repository.AppUserRepository;
import com.annztech.rewardsystem.modules.appUser.service.AppUserCompositeService;
import com.annztech.rewardsystem.modules.appUser.service.AppUserService;
import com.annztech.rewardsystem.modules.common.constants.DomainConstants;
import com.annztech.rewardsystem.modules.employee.event.AccessRestoreEvent;
import com.annztech.rewardsystem.modules.employee.event.AccessRevokeEvent;
import com.annztech.rewardsystem.modules.invite.entity.Invite;
import com.annztech.rewardsystem.modules.invite.enums.InviteStatus;
import com.annztech.rewardsystem.modules.invite.repository.InviteRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final SequenceGeneratorRepository sequenceGenerator;
    private final AppUserCompositeService appUserCompositeService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    public AppUserServiceImpl(AppUserRepository appUserRepository, AppUserMapper appUserMapper, SequenceGeneratorRepository sequenceGenerator, AppUserCompositeService appUserCompositeService, ApplicationEventPublisher applicationEventPublisher, PasswordEncoder passwordEncoder, JwtConfig jwtConfig) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
        this.sequenceGenerator = sequenceGenerator;
        this.appUserCompositeService = appUserCompositeService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public List<AppUserDTO> getAllAppUsersDTOs() {
        List<AppUser> appUsers = appUserRepository.findAll();
        return appUsers.stream().map(appUserMapper::toDto).toList();
    }

    @Override
    public AppUserDTO getAppUserDTOById(String id) {
        if(StringUtils.isBlank(id)){
            throw new AppException(AppUserConstants.USER_ID_EMPTY, HttpStatus.FORBIDDEN);
        }
        if(!ValidationUtils.isValidUUID(id)){
            throw new AppException(AppUserConstants.USER_INVALID_ID, HttpStatus.FORBIDDEN);
        }
        return appUserRepository.findById(UUID.fromString(id)).map(appUserMapper::toDto).orElseThrow(()-> new AppException(AppUserConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public AppUserIdResponseDTO createAppUser(AppUserCreateDTO appUser) {
        Invite invite = getInviteEntityById(appUser.getInviteId(), appUser.getEmail());
        AppUser appUserEntity = appUserMapper.toCreateNewEntity(appUser);
        appUserEntity.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserEntity.setUserCode(getSequenceId());
        appUserEntity.setAppCode(invite.getAppType());
        appUserEntity.setIsActive(true);
        appUserEntity.setIsDeleted(false);
        appUserEntity.setStatus(AppUserConstants.ACTIVATED);
        AppUser savedAppUser = appUserRepository.save(appUserEntity);
        //UPDATE THE USER_ID in EMPLOYEE TABLE WHEN EMPLOYEE CREATING CREDENTIALS IN THE SYSTEM
        //NOTE: THIS IS NOT APPLICABLE WHEN ADMIN CREATING CREDENTIALS (SINCE USER ID ALREADY UPDATED IN EMPLOYEE TABLE)
        if(invite.getAppType().equals(DomainConstants.EMPLOYEE_REWARD_APP)){
            updateUserId(savedAppUser.getId(), appUser.getEmail().toLowerCase());
        }
        return new AppUserIdResponseDTO(savedAppUser);
    }

    @Override
    public AppUser findUserByEmail(String email) {
        return appUserRepository.findAppUsersByEmail(email.toLowerCase()).orElseThrow(()-> new AppException(AppUserConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public void deactiveAppUser(String userId) {
        StringsUtils.validateUUID(userId, AppUserConstants.USER_ID_EMPTY);
        AppUser appUser = appUserRepository.findById(UUID.fromString(userId)).orElse(null);
        if(appUser != null){
            appUser.setIsActive(false);
            appUserRepository.save(appUser);
        }
    }

    @Override
    public void activeAppUser(String userId) {
        StringsUtils.validateUUID(userId, AppUserConstants.USER_ID_EMPTY);
        AppUser appUser = appUserRepository.findById(UUID.fromString(userId)).orElse(null);
        if(appUser != null){
            appUser.setIsActive(true);
            appUserRepository.save(appUser);
        }
    }

    @Override
    public AppUser getAppUserEntityEmailAndAppCode(String email, String appCode) {
        return appUserRepository.findAppUsersByEmailAndAppCode(email.toLowerCase(), appCode).orElse(null);
    }

    @EventListener
    @Transactional
    public void onAdminAccessRevoke(List<AccessRevokeEvent> events){
        for(AccessRevokeEvent event : events){
            AppUser appUser = appUserRepository.findAppUsersByEmailAndAppCode(event.getEmail(), event.getAppType()).orElseThrow(()-> new AppException(AppUserConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
            appUser.setStatus(AppUserConstants.BLOCKED);
            appUserRepository.save(appUser);
        }
    }

    @EventListener
    @Transactional
    public void onAdminAccessRestore(List<AccessRestoreEvent> events){
        for(AccessRestoreEvent event : events){
            AppUser appUser = appUserRepository.findAppUsersByEmailAndAppCode(event.getEmail(), event.getAppType()).orElseThrow(()-> new AppException(AppUserConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
            appUser.setStatus(AppUserConstants.ACTIVATED);
            appUserRepository.save(appUser);
        }
    }

    private Invite getInviteEntityById(String id, String email){
        if(StringUtils.isBlank(id)){
            throw new AppException(AppUserConstants.INVITE_ID_EMPTY, HttpStatus.FORBIDDEN);
        }
        if(!ValidationUtils.isValidUUID(id)){
            throw new AppException(AppUserConstants.INVALID_INVITE_ID, HttpStatus.FORBIDDEN);
        }
        Invite invite = appUserCompositeService.getInviteById(id);
        if(!invite.getReceiverEmail().equalsIgnoreCase(email)){
            throw new AppException(AppUserConstants.INVITE_EMAIL_MISMATCH, HttpStatus.FORBIDDEN);
        }
        boolean isExpire = Instant.now().isAfter(invite.getExpiresAt());
        if(isExpire){
            throw new AppException(AppUserConstants.INVITE_EXPIRED, HttpStatus.FORBIDDEN);
        }
        return invite;
    }

    private void updateUserId(UUID userId, String email){
        applicationEventPublisher.publishEvent(new EmployeeUserIdUpdateEvent(userId, email));
    }

    private String getSequenceId(){
        Long nextVal = sequenceGenerator.getNextSequence(AppUserConstants.USER_CODE_SEQ);
        return String.format(AppUserConstants.USER_CODE_FORMAT, nextVal);
    }
}
