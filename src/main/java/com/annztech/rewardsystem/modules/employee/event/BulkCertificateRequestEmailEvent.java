package com.annztech.rewardsystem.modules.employee.event;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkCertificateRequestEmailEvent {
    private List<CertificateRequestEmailEvent> emailEvents;
}