package com.annztech.rewardsystem.modules.user.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.employee.constants.EmployeeConstants;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeDTO;
import com.annztech.rewardsystem.modules.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user-management")
@RestController
@Tag(name = "User Management", description = "User Management")
public class UserController extends LocalizationService {

    private final EmployeeService employeeService;

    public UserController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Get all Certificate Templates")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetched all Certificate Templates successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class))
                    )
            ),
    })
    @GetMapping
    public ResponseEntity<Object> getUserAdmins(){
        return AppResponse.success(getMessage(EmployeeConstants.EMPLOYEE_USER_ACCESS_FETCHED), HttpStatus.OK, employeeService.getAllAdmins());
    }

}
