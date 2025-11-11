package com.annztech.rewardsystem.modules.lookups.repository;

import com.annztech.rewardsystem.modules.lookups.entity.AppTypeLookUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AppTypeLookupRepository extends JpaRepository<AppTypeLookUp, UUID> {
    @Query("SELECT u FROM AppTypeLookUp u WHERE u.code = :code")
    Optional<AppTypeLookUp> findByCode(@Param("code") String code);
}
