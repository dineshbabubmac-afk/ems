package com.annztech.rewardsystem.modules.lookups.repository;

import com.annztech.rewardsystem.modules.lookups.entity.BandLevelLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BandLevelLookUpRepository extends JpaRepository<BandLevelLookup, UUID> {
    @Query("SELECT u FROM BandLevelLookup u WHERE u.code = :code")
    Optional<BandLevelLookup> findByCode(@Param("code") String code);
}
