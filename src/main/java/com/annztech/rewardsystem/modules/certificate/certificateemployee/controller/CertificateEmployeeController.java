package com.annztech.rewardsystem.modules.certificate.certificateemployee.controller;


import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.constants.CertificateEmployeeConstants;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.service.CertificateEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public class CertificateEmployeeController extends LocalizationService {
    private final CertificateEmployeeService certificateEmployeeService;

    public CertificateEmployeeController(CertificateEmployeeService certificateEmployeeService) {
        this.certificateEmployeeService = certificateEmployeeService;
    }

    @Operation(
            summary = "Get all rewards assigned to an employee",
            description = "Retrieve the list of all certificates/rewards associated with the currently logged-in employee."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rewards fetched successfully"),
            @ApiResponse(responseCode = "404", description = "No rewards found for the employee"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/getAll-my-rewards")
    public ResponseEntity<Object> getAllRewards() {
        return AppResponse.success(
                getMessage(CertificateEmployeeConstants.REWARDS_FETCHED_SUCCESSFULLY),
                HttpStatus.OK,
                certificateEmployeeService.getAllMyRewards()
        );
    }
}
