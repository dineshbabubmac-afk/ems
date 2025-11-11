package com.annztech.rewardsystem.external.email.helper;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.modules.department.entity.Department;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeInviteDTO;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.invite.entity.Invite;
import com.annztech.rewardsystem.modules.invite.service.InviteCompositeService;
import com.annztech.rewardsystem.modules.job.entity.Job;
import com.annztech.rewardsystem.modules.location.entity.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class EmailHelper {

    private final InviteCompositeService inviteCompositeService;

    public EmailHelper(InviteCompositeService inviteCompositeService) {
        this.inviteCompositeService = inviteCompositeService;
    }

    public static String loadInviteEmailTemplate(String template) {
        try {
            ClassPathResource resource = new ClassPathResource(template);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch(Exception e){
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public EmployeeInviteDTO getEmployeeInviteData(Invite invite) {
        Employee receiver = inviteCompositeService.getEmployeeEntityByEmail(invite.getReceiverEmail());
        Department department = receiver.getDepartment();
        Job job = receiver.getJob();
        Location location = receiver.getLocation();
        return EmployeeInviteDTO.builder()
                .sendersEmail(invite.getSenderEmail())
                .receiverEmail(invite.getReceiverEmail())
                .name(receiver.getFirstName() + " " + receiver.getLastName())
                .referenceId(invite.getReferenceId().toString())
                .appType(invite.getAppType())
                .departmentName(department != null ? department.getNameEn() : "N/A")
                .jobTitle(job != null ? job.getTitleEn() : "N/A")
                .locationName(location != null ? location.getNameEn() : "N/A")
                .build();
    }

}
