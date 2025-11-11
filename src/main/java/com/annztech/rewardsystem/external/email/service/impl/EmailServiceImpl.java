package com.annztech.rewardsystem.external.email.service.impl;

import com.annztech.rewardsystem.external.email.constants.EmailConstants;
import com.annztech.rewardsystem.external.email.helper.EmailHelper;
import com.annztech.rewardsystem.external.email.service.EmailProviderClient;
import com.annztech.rewardsystem.external.email.service.EmailService;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeInviteDTO;
import com.annztech.rewardsystem.modules.employee.event.*;
import com.annztech.rewardsystem.modules.invite.dto.BulkAdminInviteEmailEvent;
import com.annztech.rewardsystem.modules.invite.entity.Invite;
import com.annztech.rewardsystem.modules.invite.event.AdminInviteEmailEvent;
import com.annztech.rewardsystem.modules.invite.event.UserInviteEmailEvent;
import com.annztech.rewardsystem.modules.invite.service.InviteService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private final EmailProviderClient emailProviderClient;
    private final InviteService inviteService;
    private final EmailHelper emailHelper;

    public EmailServiceImpl(EmailProviderClient emailProviderClient, InviteService inviteService, EmailHelper emailHelper) {
        this.emailProviderClient = emailProviderClient;
        this.inviteService = inviteService;
        this.emailHelper = emailHelper;
    }

    @Override
    public void sendEmail(String toAddress, String subject, String body) {
        emailProviderClient.sendEmail("noreply@aicybershield.net", toAddress, subject, body);
    }

    @EventListener
    public void onUserInviteEmailEvent(UserInviteEmailEvent event) {
        try {
            String template = EmailHelper.loadInviteEmailTemplate("templates/invite.html");
            Invite invite = inviteService.getInviteEntityById(event.getLink().substring(event.getLink().lastIndexOf("/") + 1));
            EmployeeInviteDTO inviteData = emailHelper.getEmployeeInviteData(invite);
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("{{receiver_name}}", inviteData.getName());
            placeholders.put("{{app_name}}", inviteData.getAppType());
            placeholders.put("{{department_name}}", inviteData.getDepartmentName());
            placeholders.put("{{job_title}}", inviteData.getJobTitle());
            placeholders.put("{{location_name}}", inviteData.getLocationName());
            placeholders.put("{{invite_link}}", event.getLink());
            placeholders.put("{{sender_name}}", event.getInviterName());
            String htmlContent = replacePlaceholders(template, placeholders);
            sendEmail(event.getToEmail(), event.getSubject(), htmlContent);
            log.info("✅ Invite email sent successfully to {}", event.getToEmail());
        } catch (Exception e) {
            log.error("❌ Failed to send invite email to {}: {}", event.getToEmail(), e.getMessage(), e);
        }
    }

    @EventListener
    public void onBulkAdminInviteEmailEvent(BulkAdminInviteEmailEvent event) {
        log.info("📤 Sending {} admin invite emails in bulk...", event.getEmails().size());
        for (AdminInviteEmailEvent emailData : event.getEmails()) {
            try {
                String template = EmailHelper.loadInviteEmailTemplate("templates/adminInvite.html");
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("{{receiver_name}}", emailData.getName());
                placeholders.put("{{receiver_email}}", emailData.getReceiverEmail());
                placeholders.put("{{department_name}}", emailData.getDepartmentName());
                placeholders.put("{{app_name}}", emailData.getAppType());
                placeholders.put("{{sender_email}}", emailData.getSenderEmail());
                placeholders.put("{{invite_link}}", emailData.getInviteLink());
                String htmlContent = replacePlaceholders(template, placeholders);
                sendEmail(emailData.getReceiverEmail(), emailData.getSubject(), htmlContent);
                log.info("✅ Sent bulk admin invite to {}", emailData.getReceiverEmail());
            } catch (Exception e) {
                log.error("❌ Failed to send bulk admin invite to {}: {}", emailData.getReceiverEmail(), e.getMessage());
            }
        }
        log.info("📦 All bulk admin invite emails processed.");
    }

    @EventListener
    @Async
    public void onBulkCertificateRequestAssignmentEvent(BulkCertificateRequestAssignmentEvent bulkEvent) {
        log.info("📧 Processing bulk certificate request emails for {} recipients", bulkEvent.getAssignmentEvents().size());
        for (CertificateRequestAssignmentEvent event : bulkEvent.getAssignmentEvents()) {
            try {
                String template = EmailHelper.loadInviteEmailTemplate("templates/certificate-request-notification.html");
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("{{receiver_name}}", event.getCommitteeMemberName());
                placeholders.put("{{nominated_by_name}}", event.getNominatedByName());
                placeholders.put("{{nominated_to_name}}", event.getNominatedToName());
                placeholders.put("{{certificate_name}}", event.getCertificateName());
                placeholders.put("{{certificate_category}}", event.getCertificateCategory());
                placeholders.put("{{request_code}}", event.getRequestCode());
                placeholders.put("{{review_link}}", EmailConstants.CERTIFICATE_REQUEST_NAVIGATION + event.getRequestCode());
                String htmlContent = replacePlaceholders(template, placeholders);
                sendEmail(event.getCommitteeMemberEmail(), EmailConstants.CERTIFICATE_REQUEST_SUBJECT, htmlContent);
                log.info("✅ Event email sent to {}", event.getCommitteeMemberEmail());
            } catch (Exception e) {
                log.error("❌ Failed to process email for {}: {}", event.getCommitteeMemberEmail(), e.getMessage(), e);
            }
        }
    }

    private String replacePlaceholders(String template, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            template = template.replace(entry.getKey(), entry.getValue() != null ? entry.getValue() : "");
        }
        return template;
    }
}