package com.annztech.rewardsystem.modules.metrics.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopEmployeesDTO {
    private String name;
    private String department;
    private Integer certificateCount;
    private String profileURL;
}