package com.annztech.rewardsystem.modules.employee.service;


import com.annztech.rewardsystem.modules.department.entity.Department;
import com.annztech.rewardsystem.modules.job.entity.Job;
import com.annztech.rewardsystem.modules.location.entity.Location;
import com.annztech.rewardsystem.modules.role.entity.Role;


public interface EmployeeCompositeService {
    Department getDepartmentById(String id);
    Location getLocationById(Long id);
    Job getJob(String id);

    Department getDepartmentByNameEn(String name);
    Location getLocationByNameEn(String name);
    Job getJobByNameEn(String name);

    Role getRoleEntityById(String id);
    Role getRoleEntityByName(String name);

}
