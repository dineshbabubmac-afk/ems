package com.annztech.rewardsystem.modules.reports.service;

import com.annztech.rewardsystem.modules.reports.dto.PagedResponse;
import com.annztech.rewardsystem.modules.reports.dto.ReportDTO;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    PagedResponse<ReportDTO> reportSearch(String certificateCategoryWise, String departmentName, String rewardType, String approveType, String startDate, String endDate, Pageable pageable);
}
