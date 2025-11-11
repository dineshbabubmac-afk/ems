package com.annztech.rewardsystem.modules.reports.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.reports.constants.ReportConstant;
import com.annztech.rewardsystem.modules.reports.dto.ReportDTO;
import com.annztech.rewardsystem.modules.reports.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportController extends LocalizationService {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Get all Reports")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetched all reports successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ReportDTO.class))
                    )
            ),
    })
    @GetMapping("/booking-date")
    public ResponseEntity<Object>  fetchBooking(
            @RequestParam(required = false, value = "category") String certificateCategoryId,
            @RequestParam(required = false, value = "departmentName") String departmentId,
            @RequestParam(required = false, value = "rewardType") String rewardType,
            @RequestParam(required = false, value = "approveType") String approveType,
            @RequestParam(required = false, value = "startDate") String startDate,
            @RequestParam(required = false, value = "toDate") String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return AppResponse.success(getMessage(ReportConstant.REPORT_FETCHED), HttpStatus.OK, reportService.reportSearch(certificateCategoryId, departmentId, rewardType, approveType, startDate, endDate, pageable));
    }
}
