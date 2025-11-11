package com.annztech.rewardsystem.modules.employee.service;

import com.annztech.rewardsystem.modules.employee.dto.EmployeeIdResponseDTO;

public interface EmployeeStateService {
    EmployeeIdResponseDTO activateEmployee(String employeeId);
    EmployeeIdResponseDTO deactivateEmployee(String employeeId);

}
