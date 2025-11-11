package com.annztech.rewardsystem.modules.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private String employeeName;
    private String certificateCategoryWise;
    private String departmentName;
//    private String rewardType;
//    private String approveType;
}