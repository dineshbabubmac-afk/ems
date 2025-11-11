//package com.annztech.rewardsystem.modules.reports.service.impl;
//
//import com.annztech.rewardsystem.modules.reports.dto.PagedResponse;
//import com.annztech.rewardsystem.modules.reports.dto.ReportDTO;
//import com.annztech.rewardsystem.modules.reports.repository.ReportRepository;
//import com.annztech.rewardsystem.modules.reports.service.ReportService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.*;
//
//@Slf4j
//@Service
//public class ReportServiceImpl implements ReportService {
//    private final ReportRepository reportRepository;
//
//    public ReportServiceImpl(ReportRepository reportRepository) {
//        this.reportRepository = reportRepository;
//    }
//
//
//    public PagedResponse<ReportDTO> reportSearch(String certificateCategoryWise,
//                                                 String departmentName,
//                                                 String rewardType,
//                                                 String approveType,
//                                                 String startDate,
//                                                 String endDate,
//                                                 Pageable pageable) {
//        {
//            Instant start = null;
//            Instant end;
//
//            try {
//                if (startDate != null && !startDate.isEmpty()) {
//                    start = Instant.parse(startDate + "T00:00:00Z");
//                }
//                if (endDate == null || endDate.isEmpty()) {
//                    end = Instant.now();
//                } else {
//                    end = Instant.parse(endDate + "T23:59:59Z");
//                }
//            } catch (Exception e) {
//                throw new IllegalArgumentException("Invalid date format");
//            }
//
//            log.info("Fetching report with parameters: certificateCategoryWise='{}', departmentName='{}', rewardType='{}', approveType='{}', start='{}', end='{}', page={}, size={}",
//                    UUID.fromString(certificateCategoryWise),
//                    UUID.fromString(departmentName),
//                    rewardType,
//                    approveType,
//                    start,
//                    end,
//                    pageable.getPageNumber(),
//                    pageable.getPageSize()
//            );
//
//            Page<ReportDTO> page = reportRepository.fetchReport(
//                    UUID.fromString(certificateCategoryWise),
//                    UUID.fromString(departmentName),
//                    rewardType,
//                    approveType,
//                    start,
//                    end,
//                    pageable
//            );
//            return new PagedResponse<>(
//                    page.getContent(),
//                    page.getTotalElements(),
//                    page.getTotalPages(),
//                    page.getNumber(),
//                    page.getSize()
//            );
//        }
//
//    }
//}


package com.annztech.rewardsystem.modules.reports.service.impl;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.reports.dto.ReportDTO;
import com.annztech.rewardsystem.modules.reports.dto.PagedResponse;
import com.annztech.rewardsystem.modules.reports.repository.ReportRepository;
import com.annztech.rewardsystem.modules.reports.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.UUID;

@Service
@Slf4j
public class ReportServiceImpl extends LocalizationService implements ReportService{

    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public PagedResponse<ReportDTO> reportSearch(String certificateCategoryWise,
                                                 String departmentName,
                                                 String rewardType,
                                                 String approveType,
                                                 String startDate,
                                                 String endDate,
                                                 Pageable pageable) {

        Instant start = null;
        Instant end = null;


            ZoneId zone = ZoneId.of("Asia/Kolkata");

            if(startDate != null && endDate == null || endDate != null && startDate == null) {
                throw new IllegalArgumentException("Both startDate and endDate must be provided together.");
            }

            if(startDate == null && endDate == null){
                startDate = "1925-11-11";
                endDate = LocalDate.now().toString();
            }

            if (startDate != null && !startDate.isBlank()) {
                start = LocalDate.parse(startDate)
                        .atStartOfDay(zone)
                        .toInstant();
            }

            if (endDate != null && !endDate.isBlank()) {
                end = LocalDate.parse(endDate)
                        .atTime(LocalTime.MAX)
                        .atZone(zone)
                        .toInstant();
            }
        log.info("""
                Fetching report with parameters:
                certificateCategoryWise='{}', departmentName='{}',
                rewardType='{}', approveType='{}',
                start='{}', end='{}', page={}, size={}
                """,
                certificateCategoryWise, departmentName,
                rewardType, approveType,
                start, end, pageable.getPageNumber(), pageable.getPageSize()
        );

        UUID categoryUUID = parseUUID(certificateCategoryWise);
        UUID departmentUUID = parseUUID(departmentName);

        Page<ReportDTO> page = reportRepository.fetchReport(
                categoryUUID,
                departmentUUID,
                rewardType,
                approveType,
                start,
                end,
                pageable
        );

        return new PagedResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    private UUID parseUUID(String value) {
        if (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) {
            return null;
        }
        return UUID.fromString(value);
    }
}
