package com.annztech.rewardsystem.modules.department.service.impl;

import com.annztech.rewardsystem.modules.department.repository.DepartmentRepository;
import com.annztech.rewardsystem.modules.department.service.DepartmentLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DepartmentLookUpServiceImpl implements DepartmentLookUpService {

    private final DepartmentRepository departmentRepository;

    public DepartmentLookUpServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Long getOverAllDepartmentsCount(Integer month, Integer year) {
        log.info("Fetching total departments count in {}/{}...", month, year);
        return departmentRepository.getOverAllDepartmentsCount(month, year);
    }
}
