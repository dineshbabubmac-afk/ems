package com.annztech.rewardsystem.modules.certificate.approval.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateCommitteeUpdateDTO {
    private String committeeMemberId;
    private String employeeCode;
}
