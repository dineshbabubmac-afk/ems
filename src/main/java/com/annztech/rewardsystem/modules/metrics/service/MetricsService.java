package com.annztech.rewardsystem.modules.metrics.service;

import com.annztech.rewardsystem.modules.metrics.dto.AdminMetricsResponseDTO;
import com.annztech.rewardsystem.modules.metrics.dto.EmployeeMetricsResponseDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public interface MetricsService {
    AdminMetricsResponseDTO getAdminOverAllStatistics(@Min(1) @Max(12) Integer month, @Min(2000) Integer year);
    EmployeeMetricsResponseDTO getEmployeeOverAllStatistics(@Min(1) @Max(12) Integer month, @Min(2000) Integer year);
}
