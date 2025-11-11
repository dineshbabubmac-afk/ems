package com.annztech.rewardsystem.modules.department.service;

import com.annztech.rewardsystem.modules.department.dto.DepartmentDTO;
import com.annztech.rewardsystem.modules.department.dto.DepartmentIdResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface DepartmentStateService {
    DepartmentDTO activateInternalDepartment(String departmentId);
    DepartmentDTO deactivateInternalDepartment(String departmentId);

}
