package com.annztech.rewardsystem.modules.lookups.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.modules.lookups.constants.LookupConstants;
import com.annztech.rewardsystem.modules.lookups.entity.AppTypeLookUp;
import com.annztech.rewardsystem.modules.lookups.repository.AppTypeLookupRepository;
import com.annztech.rewardsystem.modules.lookups.service.AppTypeLookupService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AppTypeLookupServiceImpl implements AppTypeLookupService {
    private final AppTypeLookupRepository appTypeLookupRepository;

    public AppTypeLookupServiceImpl(AppTypeLookupRepository appTypeLookupRepository) {
        this.appTypeLookupRepository = appTypeLookupRepository;
    }

    @Override
    public AppTypeLookUp getUserTypeEntityByCode(String code) {
        return appTypeLookupRepository.findByCode(code).orElseThrow(() -> new AppException(LookupConstants.USER_TYPE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
