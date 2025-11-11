package com.annztech.rewardsystem.modules.department.mapper;

import com.annztech.rewardsystem.modules.department.dto.DepartmentCreateDTO;
import com.annztech.rewardsystem.modules.department.dto.DepartmentDTO;
import com.annztech.rewardsystem.modules.department.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Department toEntity(DepartmentCreateDTO departmentDTO);
    Department toEntity(DepartmentDTO departmentDTO);
    DepartmentDTO toDto(Department department);
}
