package com.annztech.rewardsystem.modules.lookups.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.approval.roles.entity.MemberRole;
import com.annztech.rewardsystem.modules.certificate.approval.roles.repository.MemberRoleRepository;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.employee.repository.EmployeeRepository;
import com.annztech.rewardsystem.modules.lookups.service.EmployeeLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeLookUpServiceImpl implements EmployeeLookUpService {
    private final EmployeeRepository employeeRepository;
    private final MemberRoleRepository memberRoleRepository;
    public EmployeeLookUpServiceImpl(EmployeeRepository employeeRepository, MemberRoleRepository memberRoleRepository) {
        this.employeeRepository = employeeRepository;
        this.memberRoleRepository = memberRoleRepository;
    }
    @Override
    public Employee getEmployeeById(String id) {
        StringsUtils.validateUUID(id, "Invalid Employee id: " + id);
        log.info("Looking up Employee by id: {}", id);
        UUID uuid = UUID.fromString(id);
        return employeeRepository.findById(uuid).orElseThrow(() -> new AppException("Employee not found for id: " + id, HttpStatus.BAD_REQUEST));
    }

    @Override
    public MemberRole getEmployeeMemberRoleByCode(String code) {
        return memberRoleRepository.findByCode(code).orElseThrow( () -> new  AppException("Employee Role Code not found for : " + code, HttpStatus.BAD_REQUEST));
    }

    @Override
    public Map<String, Long> getDepartmentOverview(Integer month, Integer year) {
        log.info("Fetching employee count per department in {}/{}...", month, year);
        List<Object[]> results = employeeRepository.findEmployeeCountByDepartment(month, year);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1],
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Long getActiveEmployeesCount(Integer month, Integer year) {
        log.info("Fetching total active employees in {}/{}...", month, year);
        return employeeRepository.getActiveEmployeesCount(month, year);
    }
}
