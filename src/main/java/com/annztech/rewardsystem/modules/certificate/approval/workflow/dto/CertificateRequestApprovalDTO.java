package com.annztech.rewardsystem.modules.certificate.approval.workflow.dto;

import com.annztech.rewardsystem.modules.certificate.approval.workflow.entity.CertificateRequestApproval;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateRequestApprovalDTO {
    String employeeId;
    String employeeFirstName;
    String employeeLastName;
    String role;
    String assignmentComment;
    String assignmentStatus;

    public CertificateRequestApprovalDTO(CertificateRequestApproval certificateRequestApproval){
        this.employeeId = certificateRequestApproval.getMember().getId().toString();
        this.employeeFirstName = certificateRequestApproval.getMember().getFirstName();
        this.employeeLastName = certificateRequestApproval.getMember().getLastName();
        this.assignmentComment = certificateRequestApproval.getReason();
        this.assignmentStatus = certificateRequestApproval.getStatusCode().getCode();
    }
}
