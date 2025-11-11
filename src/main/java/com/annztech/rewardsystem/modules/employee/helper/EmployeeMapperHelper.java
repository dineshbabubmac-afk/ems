package com.annztech.rewardsystem.modules.employee.helper;

import com.annztech.rewardsystem.modules.employee.dto.EmployeeDTO;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeUpdateDTO;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapperHelper {



    public static Employee updateEmployeeFromDTO(Employee employee, EmployeeUpdateDTO dto) {
        if (StringUtils.isNotBlank(dto.getFirstName())) {
            employee.setFirstName(dto.getFirstName());
        }
        if (StringUtils.isNotBlank(dto.getLastName())) {
            employee.setLastName(dto.getLastName());
        }
        if (StringUtils.isNotBlank(dto.getEmail())) {
            employee.setEmail(dto.getEmail());
        }
        if (StringUtils.isNotBlank(dto.getMobileNumber())) {
            employee.setMobileNumber(dto.getMobileNumber());
        }
        if (dto.getGender() != null) {
            employee.setGender(dto.getGender());
        }
        return employee;
    }

    public static List<EmployeeDTO> mapToEmployeeDTO(List<Employee> employees, String roleCode){
        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();
        for(Employee employee : employees){
            if(employee.getRole().getCode().equals(roleCode)){
                EmployeeDTO employeeDTO = new EmployeeDTO();
                employeeDTO.setFullName(employee.getFirstName() + " " + employee.getLastName());
                employeeDTO.setId(employee.getId().toString());
                employeeDTO.setRoleName(employee.getRole().getCode());
                employeeDTOS.add(employeeDTO);
            }
        }
        return employeeDTOS;
    }

}
