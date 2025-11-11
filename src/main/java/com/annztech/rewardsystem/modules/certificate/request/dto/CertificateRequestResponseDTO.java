package com.annztech.rewardsystem.modules.certificate.request.dto;

import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateApprovalInfoDTO;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.entity.CertificateRequestApproval;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateResponseDTO;
import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateRequestResponseDTO {
    String id;
    String code;
    CertificateResponseDTO certificate;
    EmployeeDTO nominatedTo;
    EmployeeDTO nominatedBy;
    String remarks;
    String certificateStatus;
    Instant actionedAt;
    List<CriteriaRemarkDTO> criteriaList;
    List<CertificateApprovalInfoDTO> certificateApprovalDetails;
    public CertificateRequestResponseDTO(CertificateRequest entity) {
        this.id = entity.getId().toString();
        this.code = entity.getCode();
        this.certificate = new CertificateResponseDTO(entity.getCertificate());
        this.nominatedTo = new EmployeeDTO(entity.getNominatedTo(), true);
        this.nominatedBy = new EmployeeDTO(entity.getNominatedBy(), true);
        this.remarks = entity.getRemarks();
        this.certificateStatus = entity.getStatusCode().getCode();
        this.actionedAt = entity.getActionedAt();
    }
    public CertificateRequestResponseDTO(CertificateRequestApproval certificateRequestApproval){
        this(certificateRequestApproval.getCertificateRequest());
    }
}
