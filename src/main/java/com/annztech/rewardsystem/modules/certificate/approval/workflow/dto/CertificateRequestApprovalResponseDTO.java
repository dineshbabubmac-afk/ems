package com.annztech.rewardsystem.modules.certificate.approval.workflow.dto;

import com.annztech.rewardsystem.modules.certificate.approval.workflow.entity.CertificateRequestApproval;
import com.annztech.rewardsystem.modules.certificate.request.dto.CertificateRequestResponseDTO;
import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import com.annztech.rewardsystem.modules.certificate.status.entity.CertificateStatus;
import com.annztech.rewardsystem.modules.department.dto.DepartmentDTO;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeDTO;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateRequestApprovalResponseDTO {
    String id;
    EmployeeDTO employee;
    CertificateRequestResponseDTO certificateRequest;
    DepartmentDTO departmentDTO;
    String code;
    String reason;
    String createdAt;
    String updatedAt;

    public CertificateRequestApprovalResponseDTO(CertificateRequestApproval certificateRequestApproval) {
        this.id = certificateRequestApproval.getId().toString();
        this.employee = new EmployeeDTO(certificateRequestApproval.getMember());
        this.departmentDTO = new DepartmentDTO(certificateRequestApproval.getMember().getDepartment());
        this.certificateRequest = new CertificateRequestResponseDTO(certificateRequestApproval.getCertificateRequest());
        this.code = certificateRequestApproval.getStatusCode().getCode();
        this.reason = certificateRequestApproval.getReason();
        this.createdAt = certificateRequestApproval.getCreatedAt().toString();
        this.updatedAt = certificateRequestApproval.getUpdatedAt().toString();
    }
}
