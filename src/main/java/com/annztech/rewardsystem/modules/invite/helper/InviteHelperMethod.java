package com.annztech.rewardsystem.modules.invite.helper;

import com.annztech.rewardsystem.modules.employee.dto.EmployeeInviteDTO;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.invite.entity.Invite;

public class InviteHelperMethod {

    public static EmployeeInviteDTO getEmployeeInviteDTOFrom(Employee employeeEntity, Invite invite) {
        return EmployeeInviteDTO.builder()
                .sendersEmail(invite.getSenderEmail())
                .receiverEmail(invite.getReceiverEmail())
                .name(employeeEntity.getFirstName() + " " + employeeEntity.getLastName())
                .referenceId(invite.getReferenceId().toString())
                .build();
    }

}
