package com.annztech.rewardsystem.modules.invite.service;

import com.annztech.rewardsystem.modules.employee.entity.Employee;

public interface InviteCompositeService {

    Employee getEmployeeEntityByEmail(String email);
}
