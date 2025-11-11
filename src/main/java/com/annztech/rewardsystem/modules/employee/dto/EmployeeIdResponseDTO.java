package com.annztech.rewardsystem.modules.employee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeIdResponseDTO {
    String id;
    String employeeCode;
    String message;

    public EmployeeIdResponseDTO(String id, String code) {
        this.id = id;
        this.employeeCode = code;
    }
}
