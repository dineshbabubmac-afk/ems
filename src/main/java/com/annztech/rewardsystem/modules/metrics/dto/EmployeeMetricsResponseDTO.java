package com.annztech.rewardsystem.modules.metrics.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeMetricsResponseDTO {
    private Long departments;
    private Long certificates;
    private Long employees;
    private Long category;
    private Long template;
    private Long location;
    private Long jobs;
    private Long committeeHead;
    private Long committeeMember;
    private List<TopEmployeesDTO> topEmployees;
    private Map<String, Long> topDepartments;
}
