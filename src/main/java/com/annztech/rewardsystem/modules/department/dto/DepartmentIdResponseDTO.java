package com.annztech.rewardsystem.modules.department.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentIdResponseDTO {
    String id;
    String departmentCode;
    String message;

    public DepartmentIdResponseDTO(String id, String departmentCode) {
        this.id = id;
        this.departmentCode = departmentCode;
    }
}
