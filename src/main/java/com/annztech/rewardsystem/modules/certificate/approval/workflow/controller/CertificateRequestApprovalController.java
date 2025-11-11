package com.annztech.rewardsystem.modules.certificate.approval.workflow.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.constant.CertificateRequestApprovalConstant;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateRequestApprovalUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.service.CertificateRequestApprovalService;
import com.annztech.rewardsystem.modules.employee.constants.EmployeeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reward-approval")
@Tag(name = "Reward Approval", description = "Manage Certificate Reward Approval workflows")
public class CertificateRequestApprovalController extends LocalizationService {

    private final CertificateRequestApprovalService certificateRequestApprovalService;

    public CertificateRequestApprovalController(CertificateRequestApprovalService certificateRequestApprovalService) {
        this.certificateRequestApprovalService = certificateRequestApprovalService;
    }

    @Operation(
            summary = "Get reward approvals assigned to the current user",
            description = """
        Fetches all certificate request approvals assigned to the logged-in user.
        You can optionally filter results by approval status (e.g., APPROVED, PENDING),
        active/inactive state, or perform a search by employee name, department,
        or certificate category.
        If no filters are provided, all approvals for the user are returned.
        Results are paginated and sorted by creation date (newest first).
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Approvals fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Object> rewardApprovalAll(

            @Parameter(
                    name = "status",
                    description = "Approval status filter. Allowed values: APPROVED, PENDING, or REJECTED. "
                            + "If not provided, returns all approvals.",
                    example = "APPROVED"
            )
            @RequestParam(value = "status", required = false) String status,

            @Parameter(
                    name = "isActive",
                    description = "Filter by active status. True for active approvals, false for inactive.",
                    example = "true"
            )
            @RequestParam(value = "isActive", required = false) Boolean isActive,

            @Parameter(
                    name = "query",
                    description = "Search keyword to filter by department name, employee name, "
                            + "or certificate category (case-insensitive).",
                    example = "Human Resources"
            )
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return AppResponse.success(
                getMessage(EmployeeConstants.EMPLOYEE_PROJECT_ACCESS_FETCHED),
                HttpStatus.OK,
                certificateRequestApprovalService.rewardApprovalAll(status, isActive, query, pageable)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<Object> rewardApprovalMe(

            @Parameter(
                    name = "status",
                    description = "Approval status filter. Allowed values: APPROVED, PENDING, or REJECTED. "
                            + "If not provided, returns all approvals.",
                    example = "APPROVED"
            )
            @RequestParam(value = "status", required = false) String status,

            @Parameter(
                    name = "isActive",
                    description = "Filter by active status. True for active approvals, false for inactive.",
                    example = "true"
            )
            @RequestParam(value = "isActive", required = false) Boolean isActive,

            @Parameter(
                    name = "query",
                    description = "Search keyword to filter by department name, employee name, "
                            + "or certificate category (case-insensitive).",
                    example = "Human Resources"
            )
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return AppResponse.success(
                getMessage(EmployeeConstants.EMPLOYEE_PROJECT_ACCESS_FETCHED),
                HttpStatus.OK,
                certificateRequestApprovalService.getAllApproval(status, isActive, query, pageable)
        );
    }


    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get reward approvals for a specific user",
            description = "Fetches all certificate request approvals assigned to the given user ID. "
                    + "You can filter by status (APPROVED, PENDING, or ALL)."
    )
    public ResponseEntity<Object> rewardApproval(
            @Parameter(
                    description = "User ID of the committee member",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable("userId") String userId,

            @Parameter(
                    description = "Approval status filter. Allowed values: PENDING or COMPLETED. "
                            + "If not provided, returns all approvals.",
                    example = "APPROVED"
            )
            @RequestParam(value = "status", required = false) String status
    ) {
        return AppResponse.success(
                getMessage(EmployeeConstants.EMPLOYEE_PROJECT_ACCESS_FETCHED),
                HttpStatus.OK,
                certificateRequestApprovalService.rewardApproval(userId, status)
        );
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update reward approval status",
            description = "Updates the approval status of a specific certificate request approval. "
                    + "Used by committee members to approve or reject certificate requests."
    )
    public ResponseEntity<Object> updateRewardApproval(
            @Parameter(
                    description = "ID of the Certificate Request Approval record",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable("id") String id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload containing the new status code and optional reject reason",
                    required = true
            )
            @RequestBody CertificateRequestApprovalUpdateDTO updateDTO
    ) {
        return AppResponse.success(
                getMessage(EmployeeConstants.EMPLOYEE_PROJECT_UPDATE),
                HttpStatus.OK,
                certificateRequestApprovalService.updateRewardApproval(id, updateDTO)
        );
    }


    @GetMapping("/{rewardApprovalId}")
    public ResponseEntity<Object> getCertificate(@PathVariable("rewardApprovalId") String rewardApprovalId){
        return AppResponse.success(
                getMessage(CertificateRequestApprovalConstant.CERTIFICATE_REQUEST_APPROVAL),
                HttpStatus.OK,
                certificateRequestApprovalService.getCertificateRequestResponse(rewardApprovalId)
        );
    }
}
