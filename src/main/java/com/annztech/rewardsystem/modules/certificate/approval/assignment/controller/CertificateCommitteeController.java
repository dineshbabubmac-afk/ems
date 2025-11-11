package com.annztech.rewardsystem.modules.certificate.approval.assignment.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.constant.CommitteeManagementConstants;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.dto.CertificateCommitteeCreateDTO;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.dto.CertificateCommitteeIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.dto.CertificateCommitteeUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.service.CertificateCommitteeService;
import com.annztech.rewardsystem.modules.employee.constants.EmployeeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/committee-management")
@Tag(name = "Committee Management", description = "Committee Management")
public class CertificateCommitteeController extends LocalizationService {
    private final CertificateCommitteeService certificateCommitteeService;

    public CertificateCommitteeController(CertificateCommitteeService certificateCommitteeService) {
        this.certificateCommitteeService = certificateCommitteeService;
    }


    @Operation(
            summary = "Create new committee member",
            description = "Add a new committee member with details like name, designation, and department."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Committee member created successfully",
                    content = @Content(schema = @Schema(implementation = CertificateCommitteeCreateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createCommitteeMember(
            @RequestBody CertificateCommitteeCreateDTO certificateCommitteeCreateDTO) {
        return AppResponse.success(getMessage(CommitteeManagementConstants.COMMITTEE_MEMBER_CREATED), HttpStatus.OK, certificateCommitteeService.createCommitteeMember(certificateCommitteeCreateDTO));
    }

    @Operation(summary = "Get committee member by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetch details of a committee member using their unique ID.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CertificateCommitteeIdResponseDTO.class))
                    )
            ),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCommitteeMemberById(
            @Parameter(description = "Committee member UUID") @PathVariable String id) {
        return AppResponse.success(getMessage(CommitteeManagementConstants.COMMITTEE_MEMBER_FETCHED),HttpStatus.OK, certificateCommitteeService.getCommitteeMemberById(id));
    }

    @Operation(
            summary = "Update committee member",
            description = "Update details of an existing committee member."
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateCommitteeMember(
            @Parameter(description = "employee id") @PathVariable String id,
            @Valid @RequestBody CertificateCommitteeUpdateDTO dto
    ) {
        return AppResponse.success(getMessage(CommitteeManagementConstants.COMMITTEE_MEMBER_UPDATED), HttpStatus.OK, certificateCommitteeService.updateCommitteeMember(id, dto));
    }

    @Operation(
            summary = "Delete committee member",
            description = "Delete a committee member by their ID (soft or hard delete based on service logic)."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCommitteeMember(
            @Parameter(description = "Committee member UUID") @PathVariable String id
    ) {
        return AppResponse.success(getMessage(CommitteeManagementConstants.COMMITTEE_MEMBER_DELETED), HttpStatus.OK, certificateCommitteeService.deleteCommitteeMember(id));
    }
    @GetMapping("/committee-employees/{code}")
    public ResponseEntity<Object> getAllCommitteeHead(@PathVariable String code){
        return AppResponse.success(getMessage(CommitteeManagementConstants.COMMITTEE_MEMBER_FETCHED), HttpStatus.OK, certificateCommitteeService.getAllCommitteeEmployee(code));
    }

    @GetMapping("/search/committee-employees/id-department")
    public ResponseEntity<Object> searchCommitteeHeadByIdOrDepartment(
            @Parameter(description = "Search keyword (employee ID or department name)", required = false, example = "HR")
            @RequestParam(value = "q", required = false) String query, @RequestParam(value = "EMPLOYEE_CODE")  String roleCode) {
        return AppResponse.success(
                getMessage(EmployeeConstants.COMMITTEE_HEAD_FETCHED),
                HttpStatus.OK,
                certificateCommitteeService.getSearchByUsingDepartmentOrEmployeeCode(query, roleCode));
    }

    @GetMapping("/search/committee-employees/name-email")
    public ResponseEntity<Object> searchEmployeeByIdOrDepartment(
            @Parameter(description = "Search keyword (employee ID or department name)", required = false, example = "EMP123")
            @RequestParam(value = "q", required = false) String query, @RequestParam(value = "EMPLOYEE_CODE") String roleCode) {
        return AppResponse.success(
                EmployeeConstants.EMPLOYEE_FETCHED,
                HttpStatus.OK,
                certificateCommitteeService.getSearchByUsingFirstNameOrEmail(query, roleCode)
        );
    }

    @GetMapping("/search/non-committee-employees")
    public ResponseEntity<Object> searchNonCommittee(@RequestParam(value = "q", required = false) String query){
        return AppResponse.success(EmployeeConstants.EMPLOYEE_FETCHED,
                HttpStatus.OK,
                certificateCommitteeService.getSearchNonCommitteeEmployees(query)
                );
    }
}
