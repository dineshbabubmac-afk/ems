package com.annztech.rewardsystem.modules.location.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.employee.service.EmployeeService;
import com.annztech.rewardsystem.modules.location.constants.LocationConstant;
import com.annztech.rewardsystem.modules.location.dto.LocationResponseDTO;
import com.annztech.rewardsystem.modules.location.entity.Location;
import com.annztech.rewardsystem.modules.location.repository.LocationRepository;
import com.annztech.rewardsystem.modules.location.service.LocationStateService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LocationStateServiceImpl extends LocalizationService implements LocationStateService {

    private final LocationRepository locationRepository;
    private final EmployeeService employeeService;

    public LocationStateServiceImpl(LocationRepository locationRepository, EmployeeService employeeService) {
        this.locationRepository = locationRepository;
        this.employeeService = employeeService;
    }

    @Override
    public LocationResponseDTO deactivate(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new AppException(getMessage(LocationConstant.LOCATION_NO_RECORDS), HttpStatus.NOT_FOUND));

        employeeService.deactivateEmployeeEntityByLocationId(locationId);
        location.setIsDeleted(true);
        location.setIsActive(false);
        return LocationResponseDTO.builder()
                .id(locationId)
                .nameEn(location.getNameEn())
                .nameAr(location.getNameAr())
                .active(location.getIsActive())
                .build();
    }

    @Override
    public LocationResponseDTO activate(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new AppException(getMessage(LocationConstant.LOCATION_NO_RECORDS), HttpStatus.NOT_FOUND));

        employeeService.activateEmployeeEntityByLocationId(locationId);
        location.setIsActive(true);
        location.setIsDeleted(false);
        return LocationResponseDTO.builder()
                .id(locationId)
                .nameEn(location.getNameEn())
                .nameAr(location.getNameAr())
                .active(location.getIsActive())
                .build();
    }
}
