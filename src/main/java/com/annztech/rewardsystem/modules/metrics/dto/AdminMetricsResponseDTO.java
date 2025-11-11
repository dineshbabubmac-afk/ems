package com.annztech.rewardsystem.modules.metrics.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMetricsResponseDTO {
    private NominationStatusDTO nominationStatus;
    private Map<String, Long> certificatesCategoryWise;
    private Map<String, Long> departmentOverview;
    private List<TopEmployeesDTO> employees;
    private Map<String, Long> topDepartments;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NominationStatusDTO {
        private Long total;
        private Long pending;
        private Long approved;
        private Long rejected;
        private Long published;
    }
}
