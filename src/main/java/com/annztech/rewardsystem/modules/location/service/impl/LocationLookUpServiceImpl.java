package com.annztech.rewardsystem.modules.location.service.impl;

import com.annztech.rewardsystem.modules.location.repository.LocationRepository;
import com.annztech.rewardsystem.modules.location.service.LocationLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LocationLookUpServiceImpl implements LocationLookUpService {

    private final LocationRepository locationRepository;

    public LocationLookUpServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Long getLocationCount(Integer month, Integer year) {
        return locationRepository.countLocationsByMonthYear(month, year);
    }
}
