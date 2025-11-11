package com.annztech.rewardsystem.modules.employee.event;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkCertificateRequestAssignmentEvent {
    private List<CertificateRequestAssignmentEvent> assignmentEvents;
}