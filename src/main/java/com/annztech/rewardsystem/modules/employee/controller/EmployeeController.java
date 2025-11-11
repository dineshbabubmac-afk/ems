package com.annztech.rewardsystem.modules.employee.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.openApi.StandardApiResponses;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.employee.constants.EmployeeConstants;
import com.annztech.rewardsystem.modules.employee.dto.*;
import com.annztech.rewardsystem.modules.employee.service.EmployeeRoleUpdateService;
import com.annztech.rewardsystem.modules.employee.service.EmployeeService;
import com.annztech.rewardsystem.modules.employee.service.EmployeeStateService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/employees")
@RestController
@Tag(name = "Employee", description = "Employee Management")
public class EmployeeController extends LocalizationService {

    private final EmployeeService employeeService;
    private final EmployeeStateService employeeStateService;
    private final EmployeeRoleUpdateService employeeRoleUpdateService;

    public EmployeeController(EmployeeService employeeService, EmployeeStateService employeeStateService, EmployeeRoleUpdateService employeeRoleUpdateService) {
        this.employeeService = employeeService;
        this.employeeStateService = employeeStateService;
        this.employeeRoleUpdateService = employeeRoleUpdateService;
    }

    @GetMapping
    @Operation(
            summary = "Get all employees",
            description = "Returns all employees"
    )
    @StandardApiResponses
    public ResponseEntity<Object> getAllEmployees(@RequestParam(value = "filter", required = false) String query){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_FETCHED), HttpStatus.OK, employeeService.getAllEmployeeDTO(query));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get employee by ID",
            description = "Returns a employee by its unique ID"
    )
    @Parameter(description = "Employee ID", example = "123e4567-e89b-12d3-a456-426614174000")
    public ResponseEntity<Object> getEmployeeById(@PathVariable String id){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_FETCHED), HttpStatus.OK, employeeService.getEmployeeDTOById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new employee")
    @StandardApiResponses
    public ResponseEntity<Object> addEmployee(@Valid @RequestBody EmployeeCreateDTO employee){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_CREATED), HttpStatus.CREATED, employeeService.createEmployeeDTO(employee));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an employee")
    @Parameter(description = "Employee ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @StandardApiResponses
    public ResponseEntity<Object> updateEmployee(@PathVariable String id, @RequestBody EmployeeUpdateDTO employee){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_UPDATED), HttpStatus.CREATED, employeeService.updateEmployeeDTO(id, employee));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete an employee")
    @Parameter(description = "Employee ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @StandardApiResponses
    public ResponseEntity<Object> deleteEmployee(@PathVariable String id){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_DELETED), HttpStatus.OK, employeeService.deleteEmployeeDTO(id));
    }

    @PatchMapping(path="/{id}/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Uploading employee profile pic")
    @Parameter(description = "Employee ID", example = "123e4567-e89b-12d3-a456-426614174000",  name = "id",
            required = true)
    @StandardApiResponses
    public ResponseEntity<Object> updateProfilePic( @Parameter(
            description = "Profile image file (jpg, png only)",
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "string", format = "binary", example = "sample-profile.jpg")
            )
    ) @RequestPart("file") MultipartFile file, @PathVariable String id){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_PROFILE_UPLOAD), HttpStatus.CREATED, employeeService.updateProfilePicDTO(id, file));
    }

    @Operation(summary = "Get all Certificate Templates")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetched all Certificate Templates successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeIdResponseDTO.class))
                    )
            ),
    })
    @PatchMapping("/roles")
    public ResponseEntity<Object> updateEmployeeRole(@RequestBody EmployeeUpdateUserAccessDTO dto) {
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_ROLE_UPDATED), HttpStatus.CREATED, employeeRoleUpdateService.updateEmployeeRole(dto));
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<Object> getEmployeeByDepartment(@PathVariable String id){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_DEPARTMENT_FETCHED), HttpStatus.OK, employeeService.getAllEmployeeDTOByDepartmentId(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchEmployee(@RequestParam(value = "q", required = false) String query){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_FETCHED), HttpStatus.OK, employeeService.getSearchEmployeeDTO(query));
    }

    @GetMapping("/user-access/search")
    public ResponseEntity<Object> getEmployeeUserAccessSearch(@RequestParam(value = "q", required = false) String query){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_USER_ACCESS_FETCHED), HttpStatus.OK, employeeService.getEmployeeSearchByRoles(query));
    }

    @PatchMapping("/{employeeId}/deactivate")
    public ResponseEntity<Object> deactivateInternalEmployee(@PathVariable String employeeId){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_DEACTIVATED), HttpStatus.OK, employeeStateService.deactivateEmployee(employeeId));
    }

    @PatchMapping("/{employeeId}/activate")
    public ResponseEntity<Object> activateInternalEmployee( @PathVariable String employeeId){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_ACTIVATED), HttpStatus.OK, employeeStateService.activateEmployee(employeeId));
    }
    @GetMapping("/search/employee/id-department")
    @Operation(
            summary = "Search Committee Heads by Employee ID or Department",
            description = "Retrieve a list of committee heads matching the provided employee ID or department name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Committee heads retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search keyword"),
            @ApiResponse(responseCode = "404", description = "No committee heads found")
    })
    public ResponseEntity<Object> searchCommitteeHeadByIdOrDepartment(
            @Parameter(description = "Search keyword (employee ID or department name)", required = false, example = "HR")
            @RequestParam(value = "q", required = false) String query, @RequestParam(value = "EMPLOYEE_CODE")  String roleCode) {
        return AppResponse.success(
                getMessage(EmployeeConstants.COMMITTEE_HEAD_FETCHED),
                HttpStatus.OK,
                employeeService.getSearchByUsingDepartmentOrEmployeeCode(query, roleCode));
    }

    @GetMapping("/search/employee/name-email")
    @Operation(
            summary = "Search Employees by Employee ID or Department",
            description = "Retrieve a list of employees matching the provided employee ID or department name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search keyword"),
            @ApiResponse(responseCode = "404", description = "No employees found")
    })
    public ResponseEntity<Object> searchEmployeeByIdOrDepartment(
            @Parameter(description = "Search keyword (employee ID or department name)", required = false, example = "EMP123")
            @RequestParam(value = "q", required = false) String query, @RequestParam(value = "EMPLOYEE_CODE") String roleCode) {
        return AppResponse.success(
                EmployeeConstants.EMPLOYEE_FETCHED,
                HttpStatus.OK,
                employeeService.getSearchByUsingFirstNameOrEmail(query, roleCode)
        );
    }

    @GetMapping("/my-rewards")
    @Operation(summary = "Get all my reward")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieve all my rewards received.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeRewardsResponseDTO.class))
                    )
            ),
    })
    public ResponseEntity<Object> getAllRewards() {
        return AppResponse.success(EmployeeConstants.EMPLOYEE_FETCHED, HttpStatus.OK, employeeService.getAllMyRewards());
    }
    @Operation(summary = "View the Certificate Template (Render as HTML)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetched Certificate Template view successfully",
                    content = @Content(
                            mediaType = "text/html"
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid template ID"),
            @ApiResponse(responseCode = "404", description = "Certificate Template not found")
    })
    @GetMapping("/{templateId}/view")
    public ResponseEntity<String> getCertificateTemplateView(
            @PathVariable("templateId") String templateId,
            @RequestParam("id") String id) {
        return ResponseEntity.ok(employeeService.getCertificateTemplateViewById(templateId, id));
    }
}
