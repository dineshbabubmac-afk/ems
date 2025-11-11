package com.annztech.rewardsystem.modules.metrics.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.metrics.service.MetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/metrics")
@Tag(name = "Metrics", description = "Endpoints to retrieve application metrics and statistics")
public class MetricsController extends LocalizationService {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("overall-stats")
    @Operation(summary = "Get Overall Metrics for a month",
            description = "Returns overall metrics for a month")
    public ResponseEntity<Object> getAdminOverAllMetrics(
            @RequestParam @Min(1) @Max(12) Integer month,
            @RequestParam @Min(2000) Integer year
    ) {
        return AppResponse.success(getMessage("Overall Statistics fetched successfully"), HttpStatus.OK, metricsService.getAdminOverAllStatistics(month, year));
    }

//    @GetMapping("/certificate-trends")
//    @Operation(summary = "Get Certificate Trends from the start date to end date",
//            description = "Returns Certificate Trends from the start date to end date.")
//    public ResponseEntity<Object> getCertificateTrends(
//            @RequestParam @Min(1) @Max(12) Integer month,
//            @RequestParam @Min(2000) Integer year) {
//        return AppResponse.success(getMessage("Vulnerabilities for the specified month fetched successfully"), HttpStatus.OK, metricsService.getMonthlySeverityTrend(month, year));
//    }

    @GetMapping("/employee/overall-stats")
    @Operation(summary = "Get Overall Metrics",
            description = "Returns overall metrics")
    public ResponseEntity<Object> getEmployeeOverAllMetrics(
            @RequestParam @Min(1) @Max(12) Integer month,
            @RequestParam @Min(2000) Integer year
    ) {
        return AppResponse.success(getMessage("Overall Statistics fetched successfully"), HttpStatus.OK, metricsService.getEmployeeOverAllStatistics(month, year));
    }
}
