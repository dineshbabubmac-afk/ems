package com.annztech.rewardsystem.modules.department.dto;

import com.annztech.rewardsystem.modules.department.entity.Department;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateDTO {

    @NotBlank(message = "English name is required")
    private String nameEn;
    @NotBlank(message = "Arabic name is required")
    private String nameAr;

    public DepartmentCreateDTO(Department department) {
        this.nameEn = department.getNameEn();
        this.nameAr = department.getNameAr();
    }
}
