package com.annztech.rewardsystem.modules.employee.event;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateRequestEmailEvent {
    private String receiverEmail;
    private String receiverName;
    private String nominatedByName;
    private String nominatedToName;
    private String certificateName;
    private String requestCode;
    private String certificateCategory;
    private String reviewLink;
    private String subject;
}