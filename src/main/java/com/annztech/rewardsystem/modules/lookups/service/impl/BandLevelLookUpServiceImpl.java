package com.annztech.rewardsystem.modules.lookups.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.modules.lookups.entity.BandLevelLookup;
import com.annztech.rewardsystem.modules.lookups.repository.BandLevelLookUpRepository;
import com.annztech.rewardsystem.modules.lookups.service.BandLevelLookupService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BandLevelLookUpServiceImpl implements BandLevelLookupService {

    public final BandLevelLookUpRepository bandLevelLookUpRepository;

    public BandLevelLookUpServiceImpl(BandLevelLookUpRepository bandLevelLookUpRepository) {
        this.bandLevelLookUpRepository = bandLevelLookUpRepository;
    }

    @Override
    public BandLevelLookup getEntityByBandCode(String code) {
        return bandLevelLookUpRepository.findByCode(code).orElseThrow(() -> new AppException("Invalid band code", HttpStatus.BAD_REQUEST));
    }
}
