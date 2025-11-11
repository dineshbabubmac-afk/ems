package com.annztech.rewardsystem.modules.department.dto;

import com.annztech.rewardsystem.modules.department.entity.Department;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Successful response wrapping list of departments")
public class DepartmentDTO {
    private String id;
    private String nameEn;
    private String nameAr;
    private Boolean active;

    public DepartmentDTO(Department department) {
        this.id = department.getId().toString();
        this.nameEn = department.getNameEn();
        this.nameAr = department.getNameAr();
        this.active = department.getIsActive();
    }

    public DepartmentDTO(Employee employee) {
        this.id = employee.getDepartment().getId().toString();
        this.nameAr = employee.getDepartment().getNameAr();
        this.nameEn = employee.getDepartment().getNameEn();
        this.active = employee.getDepartment().getIsActive();
    }
}
