package com.annztech.rewardsystem.modules.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAccessRoleDTO {
    UUID employeeId;
    UUID roleId;
}
