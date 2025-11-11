package com.annztech.rewardsystem.modules.employee.mapper;

import com.annztech.rewardsystem.modules.employee.dto.*;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(source = "profilePicturePath", target = "profilePicture")
    @Mapping(target = "job.bandLevel", ignore = true)
    @Mapping(source = "employeeCode", target = "code")
    EmployeeDTO toDto(Employee employee);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", expression = "java(employeeDTO.getEmail().toLowerCase())")
    @Mapping(target = "firstName", expression = "java(employeeDTO.getFirstName().toLowerCase())")
    @Mapping(target = "lastName", expression = "java(employeeDTO.getLastName().toLowerCase())")
    Employee toEntity(EmployeeCreateDTO employeeDTO);
    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeUpdateDTO employeeDTO);
    EmployeeIdResponseDTO toDto(String id, String code);
    EmployeePicResponseDTO toDto(String id, String code, String profilePicturePath);
}
