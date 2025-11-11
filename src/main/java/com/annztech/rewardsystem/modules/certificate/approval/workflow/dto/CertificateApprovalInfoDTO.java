package com.annztech.rewardsystem.modules.certificate.approval.workflow.dto;


import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CertificateApprovalInfoDTO {

    private UUID certificateRequestId;
    private UUID memberId;
    private String memberFirstName;
    private String memberLastName;
    private String statusCode;
    private String reason;
    private String roleCode;
    private Instant updatedAt;

    public CertificateApprovalInfoDTO(
            UUID certificateRequestId,
            UUID memberId,
            String memberFirstName,
            String memberLastName,
            String statusCode,
            String reason,
            String roleCode,
            Instant updatedAt) {
        this.certificateRequestId = certificateRequestId;
        this.memberId = memberId;
        this.memberFirstName = memberFirstName;
        this.memberLastName = memberLastName;
        this.statusCode = statusCode;
        this.reason = reason;
        this.roleCode = roleCode;
        this.updatedAt = updatedAt;
    }
}

