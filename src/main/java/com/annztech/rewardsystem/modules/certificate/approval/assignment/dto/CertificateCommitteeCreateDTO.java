package com.annztech.rewardsystem.modules.certificate.approval.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateCommitteeCreateDTO {
    @NotBlank(message = "Committee Member Id is Required")
    private String committeeMemberId;
    @NotBlank(message = "Member Role is Required")
    private String employeeCode;
}
