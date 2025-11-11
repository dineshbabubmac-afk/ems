package com.annztech.rewardsystem.modules.certificate.approval.assignment.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.constant.CommitteeManagementConstants;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.dto.CertificateCommitteeCreateDTO;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.dto.CertificateCommitteeIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.dto.CertificateCommitteeUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.entity.CertificateCommittee;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.repository.CertificateCommitteeRepository;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.service.CertificateCommitteeService;
import com.annztech.rewardsystem.modules.certificate.approval.roles.entity.MemberRole;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeDTO;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.lookups.service.EmployeeLookUpService;
import com.annztech.rewardsystem.modules.role.constants.RoleConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateCommitteeServiceImpl implements CertificateCommitteeService {

    private final EmployeeLookUpService employeeLookUpService;
    private final CertificateCommitteeRepository certificateCommitteeRepository;

    public CertificateCommitteeServiceImpl(EmployeeLookUpService employeeLookUpService, CertificateCommitteeRepository certificateCommitteeRepository) {
        this.employeeLookUpService = employeeLookUpService;
        this.certificateCommitteeRepository = certificateCommitteeRepository;
    }

    @Override
    public CertificateCommitteeIdResponseDTO createCommitteeMember(CertificateCommitteeCreateDTO dto) {
        Employee employee = employeeLookUpService.getEmployeeById(dto.getCommitteeMemberId());
        employee.setIsActive(true);
        MemberRole memberRole = employeeLookUpService.getEmployeeMemberRoleByCode(dto.getEmployeeCode());
        CertificateCommittee committee = new CertificateCommittee();
        committee.setMember(employee);
        committee.setMemberRoleCode(memberRole);
        CertificateCommittee saved = certificateCommitteeRepository.save(committee);
        return CertificateCommitteeIdResponseDTO.builder().certificateCommitteeId(saved.getId().toString()).build();
    }

    @Override
    public CertificateCommitteeIdResponseDTO getCommitteeMemberById(String id) {
        StringsUtils.validateUUID(id, CommitteeManagementConstants.COMMITTEE_MEMBER_ID_INVALID);
        CertificateCommittee committee = certificateCommitteeRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(CommitteeManagementConstants.COMMITTEE_MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return new CertificateCommitteeIdResponseDTO(committee);
    }

    @Override
    public CertificateCommitteeIdResponseDTO updateCommitteeMember(String id, CertificateCommitteeUpdateDTO dto) {
        StringsUtils.validateUUID(dto.getCommitteeMemberId(), CommitteeManagementConstants.COMMITTEE_MEMBER_ID_INVALID);
        CertificateCommittee existing = certificateCommitteeRepository.findById(UUID.fromString(dto.getCommitteeMemberId())).orElseThrow(() -> new AppException(CommitteeManagementConstants.COMMITTEE_MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (dto.getCommitteeMemberId() != null)
            existing.setMember(employeeLookUpService.getEmployeeById(id));
        if (dto.getEmployeeCode() != null){
            MemberRole memberRole = employeeLookUpService.getEmployeeMemberRoleByCode(dto.getEmployeeCode());
            existing.setMemberRoleCode(memberRole);
        }
        CertificateCommittee updated = certificateCommitteeRepository.save(existing);
        return CertificateCommitteeIdResponseDTO.builder().certificateCommitteeId(updated.getId().toString()).build();
    }

    @Override
    public CertificateCommitteeIdResponseDTO deleteCommitteeMember(String id) {
        StringsUtils.validateUUID(id, CommitteeManagementConstants.COMMITTEE_MEMBER_ID_INVALID);
        CertificateCommittee existing = certificateCommitteeRepository.findByMember_id(UUID.fromString(id)).orElseThrow(() -> new AppException(CommitteeManagementConstants.COMMITTEE_MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));
        certificateCommitteeRepository.delete(existing);
        return CertificateCommitteeIdResponseDTO.builder().certificateCommitteeId(id).build();
    }

    @Override
    public List<EmployeeDTO> getAllCommitteeEmployee(String code) {
        List<CertificateCommittee> certificateCommittees = certificateCommitteeRepository.findAllByMemberRoleCode_Code(code);
        List<Employee> employee = new ArrayList<>();
        for(CertificateCommittee certificateCommittee : certificateCommittees){
            employee.add(certificateCommittee.getMember());
        }
        return certificateCommittees.stream().map(certificateCommittee -> new EmployeeDTO(certificateCommittee.getMember(), certificateCommittee.getId().toString())).toList();
    }

    @Override
    public List<Employee> getAllCommitteeMembers() {
        List<CertificateCommittee> certificateCommittees = certificateCommitteeRepository.findAllByMemberRoleCode_Code(RoleConstants.COMMITTEE_MEMBER);
        List<Employee> employees = new ArrayList<>();
        for(CertificateCommittee certificateCommittee : certificateCommittees){
            employees.add(certificateCommittee.getMember());
        }
        return employees;
    }

    @Override
    public List<EmployeeDTO> getSearchByUsingFirstNameOrEmail(String query, String roleCode) {
        List<CertificateCommittee> certificateCommittees = certificateCommitteeRepository.searchCommitteeEmployee(searchNullCheckQuery(query), roleCode);
        return certificateCommittees.stream().map(CertificateCommittee::getMember).map(EmployeeDTO::new).toList();
    }

    @Override
    public List<EmployeeDTO> getSearchByUsingDepartmentOrEmployeeCode(String query, String roleCode) {
        List<CertificateCommittee> certificateCommittees = certificateCommitteeRepository.searchCommitteeEmployeeByDepartmentOrEmployeeId(searchNullCheckQuery(query), roleCode);
        return certificateCommittees.stream().map(CertificateCommittee::getMember).map(EmployeeDTO::new).toList();
    }
    @Override
    public List<EmployeeDTO> getSearchNonCommitteeEmployees(String query){
        List<Employee> employees = certificateCommitteeRepository.searchNonCommitteeEmployees(searchNullCheckQuery(query));
        return employees.stream().map(employee -> new EmployeeDTO(employee.getId().toString(), employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getDepartment())).toList();
    }

    private String searchNullCheckQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "";
        }
        return query.trim();
    }
}
