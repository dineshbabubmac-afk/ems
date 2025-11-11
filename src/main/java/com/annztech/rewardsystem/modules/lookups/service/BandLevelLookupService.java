package com.annztech.rewardsystem.modules.lookups.service;

import com.annztech.rewardsystem.modules.lookups.entity.BandLevelLookup;

public interface BandLevelLookupService {
    BandLevelLookup getEntityByBandCode(String code);
}
