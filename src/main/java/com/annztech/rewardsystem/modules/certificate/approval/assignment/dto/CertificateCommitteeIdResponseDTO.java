package com.annztech.rewardsystem.modules.certificate.approval.assignment.dto;

import com.annztech.rewardsystem.modules.certificate.approval.assignment.entity.CertificateCommittee;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateCommitteeIdResponseDTO {
    String certificateCommitteeId;
    String memberId;
    public CertificateCommitteeIdResponseDTO(CertificateCommittee certificateCommittee) {
        this.certificateCommitteeId = certificateCommittee.getId().toString();
        this.memberId = certificateCommittee.getMember().toString();
    }
}
