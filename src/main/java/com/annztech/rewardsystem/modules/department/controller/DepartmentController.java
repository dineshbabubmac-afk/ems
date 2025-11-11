package com.annztech.rewardsystem.modules.department.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.openApi.StandardApiResponses;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.department.constants.DepartmentConstant;
import com.annztech.rewardsystem.modules.department.dto.DepartmentCreateDTO;
import com.annztech.rewardsystem.modules.department.dto.DepartmentDTO;
import com.annztech.rewardsystem.modules.department.dto.DepartmentUpdateDTO;
import com.annztech.rewardsystem.modules.department.service.DepartmentService;
import com.annztech.rewardsystem.modules.department.service.DepartmentStateService;
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

import java.util.List;

@RestController
@RequestMapping("/department")
@Tag(name = "Department", description = "API endpoints for managing departments.\n" +
        "        Supports CRUD operations, search, activation, and deactivation of departments.")
public class DepartmentController extends LocalizationService {
    private final DepartmentService departmentService;
    private final DepartmentStateService departmentStateService;
    public DepartmentController(DepartmentService departmentService, DepartmentStateService departmentStateService) {
        this.departmentService = departmentService;
        this.departmentStateService = departmentStateService;
    }

    @GetMapping
    @Operation(summary = "Get all departments")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetches a list of all departments with their active/inactive and deleted status.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DepartmentDTO.class))
                    )
            ),
    })
    public ResponseEntity<Object> getDepartment() {
        List<DepartmentDTO> departmentDTOList = departmentService.getAllDepartmentDTO();
        return AppResponse.success(getMessage(DepartmentConstant.DEPARTMENT_FETCHED),
                HttpStatus.OK, departmentDTOList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID", description = "Returns a department by its unique ID")
    @Parameter(description = "Department ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @StandardApiResponses
    public ResponseEntity<Object> getDepartmentById(  @PathVariable String id) {
        return AppResponse.success(getMessage(DepartmentConstant.DEPARTMENT_FETCHED),
                HttpStatus.OK, departmentService.getDepartmentDTO(id));
    }

    @PostMapping
    @Operation(summary = "Create a new department")
    @StandardApiResponses
    public ResponseEntity<Object> createDepartment(@Valid @RequestBody DepartmentCreateDTO department) {
        return AppResponse.success(getMessage(DepartmentConstant.DEPARTMENT_CREATED),
                HttpStatus.CREATED, departmentService.createDepartmentDTO(department));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a department")
    @Parameter(description = "Department ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @StandardApiResponses
    public ResponseEntity<Object> updateDepartment(@PathVariable String id, @RequestBody DepartmentUpdateDTO department) {
        return AppResponse.success(getMessage(DepartmentConstant.DEPARTMENT_UPDATED),
                HttpStatus.CREATED, departmentService.updateDepartmentDTO(id, department));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a department")
    @Parameter(description = "Department ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @StandardApiResponses
    public ResponseEntity<Object> deleteDepartment(@PathVariable String id) {
        return AppResponse.success(getMessage(DepartmentConstant.DEPARTMENT_DELETED),
                HttpStatus.OK, departmentService.deleteDepartmentDTO(id));
    }

    @GetMapping("/search")
    @Operation(summary = "search by department names")
    @StandardApiResponses
    public ResponseEntity<Object> searchDepartment(@RequestParam(value = "q", required = false) String query) {
        return AppResponse.success(getMessage(DepartmentConstant.DEPARTMENT_FETCHED),
                HttpStatus.OK, departmentService.searchDepartmentDTO(query));
    }
    @PatchMapping("/{departmentId}/deactivate")
    public ResponseEntity<Object> deactivateInternalDepartment(@PathVariable String departmentId){
        return AppResponse.success(getMessage(DepartmentConstant.DEPARTMENT_DEACTIVATED), HttpStatus.OK, departmentStateService.deactivateInternalDepartment(departmentId));
    }

    @PatchMapping("/{departmentId}/activate")
    public ResponseEntity<Object> activateInternalDepartment(@PathVariable String departmentId){
        return AppResponse.success(getMessage(DepartmentConstant.DEPARTMENT_ACTIVATED), HttpStatus.OK, departmentStateService.activateInternalDepartment(departmentId));
    }
}
