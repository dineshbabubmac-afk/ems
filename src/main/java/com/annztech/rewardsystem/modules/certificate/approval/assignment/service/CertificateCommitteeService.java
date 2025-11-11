package com.annztech.rewardsystem.modules.certificate.approval.assignment.service;

import com.annztech.rewardsystem.modules.certificate.approval.assignment.dto.CertificateCommitteeCreateDTO;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.dto.CertificateCommitteeIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.dto.CertificateCommitteeUpdateDTO;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeDTO;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import jakarta.validation.Valid;

import java.util.List;

public interface CertificateCommitteeService {
    CertificateCommitteeIdResponseDTO createCommitteeMember(CertificateCommitteeCreateDTO certificateCommitteeCreateDTO);
    CertificateCommitteeIdResponseDTO getCommitteeMemberById(String id);
    CertificateCommitteeIdResponseDTO updateCommitteeMember(String id, CertificateCommitteeUpdateDTO dto);
    CertificateCommitteeIdResponseDTO deleteCommitteeMember(String id);

    List<EmployeeDTO> getAllCommitteeEmployee(String code);
    List<Employee> getAllCommitteeMembers();

    List<EmployeeDTO> getSearchByUsingFirstNameOrEmail(String query, String roleCode);
    List<EmployeeDTO> getSearchByUsingDepartmentOrEmployeeCode(String query, String roleCode);

    List<EmployeeDTO> getSearchNonCommitteeEmployees(String query);
}
