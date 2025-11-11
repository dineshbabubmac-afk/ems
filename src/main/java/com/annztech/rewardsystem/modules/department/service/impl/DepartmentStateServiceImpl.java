package com.annztech.rewardsystem.modules.department.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.department.constants.DepartmentConstant;
import com.annztech.rewardsystem.modules.department.dto.DepartmentDTO;
import com.annztech.rewardsystem.modules.department.entity.Department;
import com.annztech.rewardsystem.modules.department.repository.DepartmentRepository;
import com.annztech.rewardsystem.modules.department.service.DepartmentStateService;
import com.annztech.rewardsystem.modules.employee.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class DepartmentStateServiceImpl extends LocalizationService implements DepartmentStateService {
   private final DepartmentRepository departmentRepository;
   private final EmployeeService employeeService;
    public DepartmentStateServiceImpl(DepartmentRepository departmentRepository, EmployeeService employeeService) {
        this.departmentRepository = departmentRepository;
        this.employeeService = employeeService;
    }
    @Override
    public DepartmentDTO activateInternalDepartment(String departmentId) {
        Department department = getDepartment(departmentId);
        department.setIsActive(true);
        department.setIsDeleted(false);
        employeeService.activateEmployeeEntityByDepartmentId(departmentId);
        departmentRepository.save(department);
        return getDepartmentDTO(department);
    }

    @Override
    public DepartmentDTO deactivateInternalDepartment(String departmentId) {
        Department department = getDepartment(departmentId);
        department.setIsActive(false);
        department.setIsDeleted(true);
        employeeService.deactivateEmployeeEntityByDepartmentId(departmentId);
        departmentRepository.save(department);
        return getDepartmentDTO(department);
    }

    private Department getDepartment(String departmentId) {
        StringsUtils.validateUUID(departmentId, DepartmentConstant.DEPARTMENT_ID_INVALID);
        UUID employeeUuid = UUID.fromString(departmentId);
        Department department = departmentRepository.findDepartmentById(employeeUuid);
        if (department == null) {
            throw new AppException(getMessage(DepartmentConstant.DEPARTMENT_NO_RECORDS), HttpStatus.NOT_FOUND);
        }
        return department;
    }

    private DepartmentDTO getDepartmentDTO(Department department) {
        return new DepartmentDTO(department);
    }
}
