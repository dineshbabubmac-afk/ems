package com.annztech.rewardsystem.modules.department.service;

import com.annztech.rewardsystem.modules.department.dto.DepartmentCreateDTO;
import com.annztech.rewardsystem.modules.department.dto.DepartmentDTO;
import com.annztech.rewardsystem.modules.department.dto.DepartmentUpdateDTO;
import com.annztech.rewardsystem.modules.department.entity.Department;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    DepartmentDTO createDepartmentDTO(DepartmentCreateDTO department);
    DepartmentDTO updateDepartmentDTO(String id, DepartmentUpdateDTO department);
    DepartmentDTO getDepartmentDTO(String id);
    List<DepartmentDTO> getAllDepartmentDTO();
    DepartmentDTO deleteDepartmentDTO(String id);


    //For composite service communication
    Department getDepartmentEntityById(String id);
    Department getDepartmentEntityByNameEn(String name);
    List<DepartmentDTO> searchDepartmentDTO(String query);
}
