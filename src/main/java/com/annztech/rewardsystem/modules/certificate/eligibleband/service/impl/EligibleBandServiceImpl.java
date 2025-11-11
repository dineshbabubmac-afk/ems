package com.annztech.rewardsystem.modules.certificate.eligibleband.service.impl;

import com.annztech.rewardsystem.modules.certificate.eligibleband.repository.EligibleBandRepository;
import com.annztech.rewardsystem.modules.certificate.eligibleband.service.EligibleBandService;
import org.springframework.stereotype.Service;

@Service
public class EligibleBandServiceImpl implements EligibleBandService {
    private final EligibleBandRepository eligibleBandRepository;
    public EligibleBandServiceImpl(EligibleBandRepository eligibleBandRepository) {
        this.eligibleBandRepository = eligibleBandRepository;
    }
}
