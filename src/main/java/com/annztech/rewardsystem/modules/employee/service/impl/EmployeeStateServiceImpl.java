package com.annztech.rewardsystem.modules.employee.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.employee.constants.EmployeeConstants;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeIdResponseDTO;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.employee.repository.EmployeeRepository;
import com.annztech.rewardsystem.modules.employee.service.EmployeeStateService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeStateServiceImpl extends LocalizationService implements EmployeeStateService {
    private final EmployeeRepository employeeRepository;

    public EmployeeStateServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeIdResponseDTO activateEmployee(String employeeId) {
        Employee employee = getEmployee(employeeId);
        employee.setIsActive(true);
        employee.setIsDeleted(false);
        employeeRepository.save(employee);
        return getEmployeeDTO(employee);
    }

    @Override
    public EmployeeIdResponseDTO deactivateEmployee(String employeeId) {
        Employee employee = getEmployee(employeeId);
        employee.setIsActive(false);
        employee.setIsDeleted(true);
        employeeRepository.save(employee);
        return getEmployeeDTO(employee);
    }

    private EmployeeIdResponseDTO getEmployeeDTO(Employee employee) {
        return EmployeeIdResponseDTO.builder().employeeCode(employee.getEmployeeCode()).id(employee.getId().toString()).build();
    }
    private Employee getEmployee(String employeeId) {
        StringsUtils.validateUUID(employeeId, EmployeeConstants.EMPLOYEE_INVALID_ID);
        UUID employeeUuid = UUID.fromString(employeeId);
        Employee employee = employeeRepository.findEmployeeById(employeeUuid);
        if (employee == null) {
            throw new AppException(getMessage(EmployeeConstants.EMPLOYEE_NO_RECORDS), HttpStatus.NOT_FOUND);
        }
        return employee;
    }
}
