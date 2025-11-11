package com.annztech.rewardsystem.modules.employee.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateRequestAssignmentEvent {
    private String committeeMemberEmail;
    private String committeeMemberId;
    private String committeeMemberName;
    private String nominatedByName;
    private String nominatedToName;
    private String certificateName;
    private String requestCode;
    private String certificateCategory;
}
