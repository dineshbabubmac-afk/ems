package com.annztech.rewardsystem.modules.invite.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.common.utils.ValidationUtils;
import com.annztech.rewardsystem.modules.common.constants.DomainConstants;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeInviteDTO;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.employee.event.AdminAccessInviteEvent;
import com.annztech.rewardsystem.modules.employee.event.NewEmployeeInviteEvent;
import com.annztech.rewardsystem.modules.invite.constants.InviteConstants;
import com.annztech.rewardsystem.modules.invite.dto.BulkAdminAccessInviteEvent;
import com.annztech.rewardsystem.modules.invite.dto.BulkAdminInviteEmailEvent;
import com.annztech.rewardsystem.modules.invite.dto.InviteDTO;
import com.annztech.rewardsystem.modules.invite.dto.InviteUpdateDTO;
import com.annztech.rewardsystem.modules.invite.entity.Invite;
import com.annztech.rewardsystem.modules.invite.enums.InviteStatus;
import com.annztech.rewardsystem.modules.invite.event.AdminInviteEmailEvent;
import com.annztech.rewardsystem.modules.invite.event.UserDeleteInviteEvent;
import com.annztech.rewardsystem.modules.invite.event.UserInviteEmailEvent;
import com.annztech.rewardsystem.modules.invite.helper.InviteHelperMethod;
import com.annztech.rewardsystem.modules.invite.repository.InviteRepository;
import com.annztech.rewardsystem.modules.invite.service.InviteCompositeService;
import com.annztech.rewardsystem.modules.invite.service.InviteService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class InviteServiceImpl extends LocalizationService implements InviteService {
    private final InviteRepository inviteRepository;
    private final InviteCompositeService inviteCompositeService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public InviteServiceImpl(InviteRepository inviteRepository, InviteCompositeService inviteCompositeService, ApplicationEventPublisher applicationEventPublisher) {
        this.inviteRepository = inviteRepository;
        this.inviteCompositeService = inviteCompositeService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private void publishEmailEvent(EmployeeInviteDTO employee, UUID inviteId) {
        UserInviteEmailEvent event = UserInviteEmailEvent.builder().toEmail(employee.getReceiverEmail()).
                fromEmail(employee.getSendersEmail()).
                subject(DomainConstants.INVITE_TO_JOIN_REWARD_APP).
                link(DomainConstants.EMP_INVITE_LINK + inviteId).
                inviterName(DomainConstants.INVITE_COMPANY_PLATFORM).
                inviteeName(employee.getName())
                .build();
        applicationEventPublisher.publishEvent(event);
    }

    @EventListener
    @Transactional
    public void onBulkAdminAccessInviteEvent(BulkAdminAccessInviteEvent bulkEvent) {
        log.info("📦 Received bulk admin access invite event with {} invites", bulkEvent.getInvites().size());
        if (bulkEvent == null || bulkEvent.getInvites() == null || bulkEvent.getInvites().isEmpty()) {
            return;
        }
        List<Invite> toSave = new ArrayList<>();
        List<AdminInviteEmailEvent> emailEvents = new ArrayList<>();
        for (AdminAccessInviteEvent triggerEvent : bulkEvent.getInvites()) {
            Invite invite = new Invite();
            invite.setSenderEmail(triggerEvent.getSenderEmail());
            invite.setReceiverEmail(triggerEvent.getReceiverEmail());
            invite.setReferenceId(UUID.fromString(triggerEvent.getReferenceId()));
            invite.setAppType(triggerEvent.getAppType());
            invite.setStatus(InviteStatus.PENDING.toString());
            invite.setIsActive(true);
            invite.setResendCount(0);
            invite.setExpiresAt(Instant.now().plus(120, ChronoUnit.HOURS));
            toSave.add(invite);
        }
        List<Invite> savedInvites = inviteRepository.saveAll(toSave);
        for (int i = 0; i < savedInvites.size(); i++) {
            Invite saved = savedInvites.get(i);
            AdminAccessInviteEvent trigger = bulkEvent.getInvites().get(i);
            String inviteLink = DomainConstants.ADMIN_INVITE_LINK + saved.getId();
            String subject = DomainConstants.INVITE_TO_JOIN_ADMIN_APP;
            AdminInviteEmailEvent emailEvent = AdminInviteEmailEvent.builder()
                    .receiverEmail(trigger.getReceiverEmail())
                    .senderEmail(trigger.getSenderEmail())
                    .departmentName(trigger.getDepartmentName())
                    .appType(trigger.getAppType())
                    .name(trigger.getName())
                    .inviteLink(inviteLink)
                    .subject(subject)
                    .build();
            emailEvents.add(emailEvent);
        }
        BulkAdminInviteEmailEvent bulkEmailEvent = new BulkAdminInviteEmailEvent(emailEvents);
        applicationEventPublisher.publishEvent(bulkEmailEvent);
        log.info("✅ Published bulk admin invite email event for {} recipients", emailEvents.size());
    }

    private void publishDeleteEvent(String email) {
        applicationEventPublisher.publishEvent(new UserDeleteInviteEvent(email));
    }

    @EventListener
    @Transactional
    void eventListener(NewEmployeeInviteEvent event) {
        Invite invite = constructInvite(event.getEmployeeInviteDTO());
        Invite savedEntity = inviteRepository.save(invite);
        publishEmailEvent(event.getEmployeeInviteDTO(), savedEntity.getId());
    }

    private Invite constructInvite(EmployeeInviteDTO dto) {
        Invite invite = new Invite();
        invite.setSenderEmail(dto.getSendersEmail());
        invite.setReceiverEmail(dto.getReceiverEmail());
        invite.setReferenceId(UUID.fromString(dto.getReferenceId()));
        invite.setStatus(InviteStatus.PENDING.toString());
        invite.setIsActive(true);
        invite.setResendCount(0);
        invite.setAppType(dto.getAppType());
        invite.setExpiresAt(Instant.now().plus(120, ChronoUnit.HOURS)); //will expire in 5 days
        return invite;
    }

    @Override
    public InviteDTO getInviteDTOById(String id) {
        if(StringUtils.isBlank(id)){
            throw new AppException(getMessage(InviteConstants.INVITE_ID_EMPTY), HttpStatus.BAD_REQUEST);
        }
        if(!ValidationUtils.isValidUUID(id)){
            throw new AppException(getMessage(InviteConstants.INVITE_INVALID_ID), HttpStatus.BAD_REQUEST);
        }
        Invite invite = inviteRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(getMessage(InviteConstants.INVITE_NOT_FOUND), HttpStatus.BAD_REQUEST));
        if(invite.getIsActive() == null || !invite.getIsActive()){
            throw new AppException(getMessage(InviteConstants.INVITE_EXPIRED), HttpStatus.BAD_REQUEST);
        }
        invite.setStatus(InviteStatus.VIEWED.toString());
        inviteRepository.save(invite);
        Employee sender = inviteCompositeService.getEmployeeEntityByEmail(invite.getSenderEmail());
        InviteDTO inviteDTO = new InviteDTO(invite);
        inviteDTO.setInvitedBy(StringsUtils.capitalizeName(sender.getFirstName()) + " " + StringsUtils.capitalizeName(sender.getLastName()));
        if(invite.getAppType().equalsIgnoreCase(DomainConstants.EMPLOYEE_REWARD_APP)) { inviteDTO.setInvitedTo(InviteConstants.REWARD_APPLICATION); }
        else { inviteDTO.setInvitedTo(InviteConstants.ADMIN_CONSOLE); }
        return inviteDTO;
    }

    public Invite getInviteEntityById(String id) {
        if(StringUtils.isBlank(id)){
            throw new AppException(getMessage(InviteConstants.INVITE_ID_EMPTY), HttpStatus.BAD_REQUEST);
        }
        if(!ValidationUtils.isValidUUID(id)){
            throw new AppException(getMessage(InviteConstants.INVITE_INVALID_ID), HttpStatus.BAD_REQUEST);
        }
        return inviteRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(getMessage(InviteConstants.INVITE_NOT_FOUND), HttpStatus.BAD_REQUEST));
    }

    public Invite getInviteEntityByClientIdAndId(String clientId, String id) {
        if(StringUtils.isBlank(id)){
            throw new AppException(getMessage(InviteConstants.INVITE_ID_EMPTY), HttpStatus.BAD_REQUEST);
        }
        if(!ValidationUtils.isValidUUID(id)){
            throw new AppException(getMessage(InviteConstants.INVITE_INVALID_ID), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(clientId)){
            throw new AppException(getMessage(InviteConstants.INVITE_CLIENT_ID_EMPTY), HttpStatus.BAD_REQUEST);
        }
        if(!ValidationUtils.isValidUUID(clientId)){
            throw new AppException(getMessage(InviteConstants.INVITE_CLIENT_INVALID_ID), HttpStatus.BAD_REQUEST);
        }
        return inviteRepository.findInvitesByReferenceIdAndId(UUID.fromString(clientId), UUID.fromString(id)).orElseThrow(() -> new AppException(getMessage(InviteConstants.INVITE_NOT_FOUND), HttpStatus.BAD_REQUEST));
    }

    @Override
    public InviteDTO updateInviteStatus(String id, InviteUpdateDTO dto) {
        if(StringUtils.isBlank(id)){
            throw new AppException(getMessage(InviteConstants.INVITE_ID_EMPTY), HttpStatus.BAD_REQUEST);
        }
        if(!ValidationUtils.isValidUUID(id)){
            throw new AppException(getMessage(InviteConstants.INVITE_INVALID_ID), HttpStatus.BAD_REQUEST);
        }
        Invite invite = getInviteEntityById(id);
        return getUpdateInvite(invite, dto);
    }

    @Transactional
    @Override
    public InviteDTO resendUpdateStatus(String id) {
        if(StringUtils.isBlank(id)){
            throw new AppException(getMessage(InviteConstants.INVITE_ID_EMPTY), HttpStatus.BAD_REQUEST);
        }
        if(!ValidationUtils.isValidUUID(id)){
            throw new AppException(getMessage(InviteConstants.INVITE_INVALID_ID), HttpStatus.BAD_REQUEST);
        }
        Invite invite = getInviteEntityById(id);
        return getResendInviteDTO(invite);
    }

    @Override
    public List<InviteDTO> getAllInviteDTO() {
       List<Invite> inviteList = inviteRepository.findAll();
       return inviteList.stream().map(InviteDTO::new).toList();
    }

    // WARNING: This operation needs to check
    // SOFT DELETE OR FULL DELETE
    @Override
    @Transactional
    public InviteDTO deleteInvite(String id) {
//        if(StringUtils.isBlank(id)){
//            throw new AppException(getMessage(InviteConstants.INVITE_ID_EMPTY), HttpStatus.BAD_REQUEST);
//        }
//        if(!ValidationUtils.isValidUUID(id)){
//            throw new AppException(getMessage(InviteConstants.INVITE_INVALID_ID), HttpStatus.BAD_REQUEST);
//        }
//        Invite invite = getInviteEntityById(id);
//        publishDeleteEvent(invite.getReceiverEmail());
        return null;
    }

    private InviteDTO getUpdateInvite(Invite invite, InviteUpdateDTO dto){
        boolean isExpired = invite.getExpiresAt().isBefore(Instant.now());
        if(isExpired){
            throw new AppException(getMessage(InviteConstants.INVITE_EXPIRED), HttpStatus.BAD_REQUEST);
        }
        String inviteStatus = dto.isAccept() ? InviteStatus.ACCEPTED.toString() : InviteStatus.REJECTED.toString();
        invite.setIsActive(false);
        invite.setStatus(inviteStatus);
        inviteRepository.save(invite);
        Employee sender = inviteCompositeService.getEmployeeEntityByEmail(invite.getReceiverEmail());
        String path = sender.getUserId() == null ? "/register" : "/login";
        String receiverEmail = invite.getReceiverEmail();
        String appCode = invite.getAppType();
        InviteDTO inviteDTO = new InviteDTO();
        if(dto.isAccept()){
            inviteDTO.setPath(path);
            inviteDTO.setReceiverEmail(receiverEmail);
            inviteDTO.setAppCode(appCode);
        }
        return inviteDTO;
    }

    private InviteDTO getResendInviteDTO(Invite invite){
        invite.setIsActive(true);
        invite.setStatus(InviteStatus.PENDING.toString());
        invite.setExpiresAt(Instant.now().plus(5, ChronoUnit.DAYS));
        inviteRepository.save(invite);
        Employee employeeEntity = inviteCompositeService.getEmployeeEntityByEmail(invite.getReceiverEmail());
        publishEmailEvent(InviteHelperMethod.getEmployeeInviteDTOFrom(employeeEntity, invite), invite.getId());
        return new InviteDTO(invite);
    }

}