package com.annztech.rewardsystem.modules.certificate.approval.workflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequestApprovalUpdateDTO {
    @NotBlank(message = "Status code is required")
    private String statusCode;
    private String rejectReason;
}
