package com.annztech.rewardsystem.modules.employee.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeInviteDTO {
    private String sendersEmail;
    private String receiverEmail;
    private String name;
    private String referenceId;
    private String appType;
    private String departmentName;
    private String jobTitle;
    private String locationName;
}