package com.annztech.rewardsystem.modules.certificate.eligibleband.repository;

import com.annztech.rewardsystem.modules.certificate.eligibleband.entity.EligibleBand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EligibleBandRepository extends JpaRepository<EligibleBand, UUID> {
}
