package com.annztech.rewardsystem.modules.employee.service.impl;

import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.department.entity.Department;
import com.annztech.rewardsystem.modules.department.service.DepartmentService;
import com.annztech.rewardsystem.modules.employee.service.EmployeeCompositeService;
import com.annztech.rewardsystem.modules.job.entity.Job;
import com.annztech.rewardsystem.modules.job.service.JobService;
import com.annztech.rewardsystem.modules.location.entity.Location;
import com.annztech.rewardsystem.modules.location.service.LocationService;
import com.annztech.rewardsystem.modules.role.entity.Role;
import com.annztech.rewardsystem.modules.role.service.RoleService;
import org.springframework.stereotype.Service;


@Service
public class EmployeeCompositeServiceImpl extends LocalizationService implements EmployeeCompositeService {

    private final LocationService locationService;
    private final JobService jobService;
    private final DepartmentService departmentService;
    private final RoleService roleService;

    public EmployeeCompositeServiceImpl(LocationService locationService, JobService jobService, DepartmentService departmentService, RoleService roleService) {
        this.locationService = locationService;
        this.jobService = jobService;
        this.departmentService = departmentService;
        this.roleService = roleService;
    }

    public Department getDepartmentById(String id) {
        return departmentService.getDepartmentEntityById(id);
    }

    public Location getLocationById(Long id) {
        return locationService.getLocationEntityById(id);
    }

    public Job getJob(String id) {return jobService.getJobEntityById(id);}

    @Override
    public Department getDepartmentByNameEn(String name) {
        return departmentService.getDepartmentEntityByNameEn(name);
    }

    @Override
    public Location getLocationByNameEn(String name) {
        return locationService.getLocationEntityByNameEn(name);
    }

    @Override
    public Job getJobByNameEn(String name) {return jobService.getJobEntityByTitleEn(name);}

    @Override
    public Role getRoleEntityById(String id) {
        return roleService.getRoleEntityById(id);
    }

    @Override
    public Role getRoleEntityByName(String name) {
        return roleService.getRoleEntityByName(name) ;
    }


}
