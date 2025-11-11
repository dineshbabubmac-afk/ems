package com.annztech.rewardsystem.modules.invite.service.impl;

import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.employee.service.EmployeeService;
import com.annztech.rewardsystem.modules.invite.service.InviteCompositeService;
import org.springframework.stereotype.Service;

@Service
public class InviteCompositeServiceImpl implements InviteCompositeService {
    private final EmployeeService employeeService;

    public InviteCompositeServiceImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public Employee getEmployeeEntityByEmail(String email) {
        return employeeService.getEmployeeEntityByEmail(email);
    }

}
