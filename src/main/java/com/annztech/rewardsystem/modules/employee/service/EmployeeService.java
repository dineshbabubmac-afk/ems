package com.annztech.rewardsystem.modules.employee.service;

import com.annztech.rewardsystem.modules.department.dto.DepartmentDTO;
import com.annztech.rewardsystem.modules.employee.dto.*;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployeeDTO(String query);
    List<EmployeeDTO> getAllEmployeeDTOByDepartmentId(String departmentId);
    List<EmployeeDTO> getAllEmployeeDTOByRoles(String query);
    List<EmployeeDTO> getAllInternalEmployeeDTO();
    EmployeeDTO getEmployeeDTOById(String id);
    EmployeeIdResponseDTO createEmployeeDTO(EmployeeCreateDTO employeeCreateDTO);
    EmployeeIdResponseDTO updateEmployeeDTO(String id, EmployeeUpdateDTO employeeUpdateDTO);
    EmployeeIdResponseDTO deleteEmployeeDTO(String id);
    EmployeePicResponseDTO updateProfilePicDTO(String id, MultipartFile file);

    List<EmployeeDTO> getSearchEmployeeDTO(String query);
    Employee getEmployeeEntityByEmail(String email);
    Employee getEmployeeEntityById(String id);
    List<EmployeeDTO> getEmployeeSearchByRoles(String query);

    void activateEmployeeEntityByLocationId(Long locationId);
    void deactivateEmployeeEntityByLocationId(Long locationId);
    void activateEmployeeEntityByJobId(String jobId);
    void deactivateEmployeeEntityByJobId(String jobId);
    void activateEmployeeEntityByDepartmentId(String departmentId);
    void deactivateEmployeeEntityByDepartmentId(String departmentId);

    List<EmployeeDTO> getAllAdmins();
    List<EmployeeRewardsResponseDTO> getAllMyRewards();
    List<EmployeeDTO> getSearchByUsingFirstNameOrEmail(String query, String roleCode);
    List<DepartmentDTO> getSearchByUsingDepartmentOrEmployeeCode(String query, String roleCode);

    String getCertificateTemplateViewById(String templateId, String id);
}
