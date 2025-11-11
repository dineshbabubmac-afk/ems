package com.annztech.rewardsystem.modules.lookups.service;

import com.annztech.rewardsystem.modules.lookups.entity.AppTypeLookUp;

public interface AppTypeLookupService {
    AppTypeLookUp getUserTypeEntityByCode(String code);
}
