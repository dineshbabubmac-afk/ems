package com.annztech.rewardsystem.modules.department.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import com.annztech.rewardsystem.modules.department.constants.DepartmentConstant;
import com.annztech.rewardsystem.modules.department.dto.DepartmentCreateDTO;
import com.annztech.rewardsystem.modules.department.dto.DepartmentDTO;
import com.annztech.rewardsystem.modules.department.dto.DepartmentUpdateDTO;
import com.annztech.rewardsystem.modules.department.entity.Department;
import com.annztech.rewardsystem.modules.department.mapper.DepartmentMapper;
import com.annztech.rewardsystem.modules.department.repository.DepartmentRepository;
import com.annztech.rewardsystem.modules.department.service.DepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentServiceImpl extends LocalizationService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public Department getDepartmentEntityById(String id) {
        return validateAndGetDepartment(id);
    }

    @Override
    public Department getDepartmentEntityByNameEn(String name) {
        if (StringUtils.isBlank(name)) {
            throw new AppException(getMessage(DepartmentConstant.DEPARTMENT_ID_REQUIRED), HttpStatus.BAD_REQUEST);
        }
        return departmentRepository.findDepartmentByNameEn(name).orElseThrow(() -> new AppException(getMessage(DepartmentConstant.DEPARTMENT_NO_RECORDS), HttpStatus.NOT_FOUND));
    }

    @Override
    public List<DepartmentDTO> searchDepartmentDTO(String query) {
        return departmentRepository.searchByKeyword(query)
                .stream()
                .map(DepartmentDTO::new)
                .toList();
    }

    //DEPARTMENT MANAGEMENT FOR EMPLOYEE ADMIN
    @Override
    public DepartmentDTO createDepartmentDTO(DepartmentCreateDTO departmentDTO) {
        Department departmentEntity = departmentMapper.toEntity(departmentDTO);
        departmentEntity.setIsActive(true);
        Department savedDepartment = departmentRepository.save(departmentEntity);
        return new DepartmentDTO(savedDepartment);
    }

    @Override
    public DepartmentDTO updateDepartmentDTO(String id, DepartmentUpdateDTO departmentUpdateDTO) {
        Department department = validateAndGetDepartment(id);
        if (StringUtils.isNotBlank(departmentUpdateDTO.getNameEn())) {
            department.setNameEn(departmentUpdateDTO.getNameEn());
        }
        if (StringUtils.isNotBlank(departmentUpdateDTO.getNameAr())) {
            department.setNameAr(departmentUpdateDTO.getNameAr());
        }
        return new DepartmentDTO(departmentRepository.save(department));
    }

    @Override
    public DepartmentDTO deleteDepartmentDTO(String id) {
        StringsUtils.validateUUID(id, DepartmentConstant.DEPARTMENT_ID_REQUIRED);
        UUID deptId = UUID.fromString(id);
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new AppException(getMessage(DepartmentConstant.DEPARTMENT_NO_RECORDS), HttpStatus.NOT_FOUND));
        department.setIsDeleted(true);
        department.setIsActive(false);
        departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    public DepartmentDTO getDepartmentDTO(String id) {
        StringsUtils.validateUUID(id, DepartmentConstant.DEPARTMENT_ID_REQUIRED);
        Department department = validateAndGetDepartment(id);
        return departmentMapper.toDto(department);
    }

    @Override
    public List<DepartmentDTO> getAllDepartmentDTO() {
        List<Department> departments = departmentRepository.findAllDepartments(DomainHelper.sortByUpdatedAtDesc());
        List<DepartmentDTO> departmentDTOS = departments.stream().map(DepartmentDTO::new).toList();
        if(departmentDTOS.isEmpty()){
            throw new AppException(getMessage(DepartmentConstant.DEPARTMENT_NO_RECORDS), HttpStatus.NOT_FOUND);
        }
        return departmentDTOS;
    }

    private Department validateAndGetDepartment(String id) {
        StringsUtils.validateUUID(id, DepartmentConstant.DEPARTMENT_ID_REQUIRED);
        return departmentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(getMessage(DepartmentConstant.DEPARTMENT_NO_RECORDS), HttpStatus.NOT_FOUND));
    }
}
