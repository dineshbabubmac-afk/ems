package com.annztech.rewardsystem.modules.employee.service;

import com.annztech.rewardsystem.modules.employee.dto.EmployeeIdResponseDTO;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeUpdateUserAccessDTO;

public interface EmployeeRoleUpdateService {
    EmployeeIdResponseDTO updateEmployeeRole(EmployeeUpdateUserAccessDTO dto);
}
