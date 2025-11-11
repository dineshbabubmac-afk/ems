package com.annztech.rewardsystem.modules.department.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentUpdateDTO {
    private String nameEn;
    private String nameAr;
}
