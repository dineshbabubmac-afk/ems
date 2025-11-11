package com.annztech.rewardsystem.modules.lookups.service;

import com.annztech.rewardsystem.modules.certificate.approval.roles.entity.MemberRole;
import com.annztech.rewardsystem.modules.employee.entity.Employee;

import java.util.Map;

public interface EmployeeLookUpService {
    Employee getEmployeeById(String id);
    MemberRole getEmployeeMemberRoleByCode(String role);
    Map<String, Long> getDepartmentOverview(Integer month, Integer year);
    Long getActiveEmployeesCount(Integer month, Integer year);
}