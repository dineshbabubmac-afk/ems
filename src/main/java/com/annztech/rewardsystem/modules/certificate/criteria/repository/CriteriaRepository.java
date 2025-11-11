package com.annztech.rewardsystem.modules.certificate.criteria.repository;

import com.annztech.rewardsystem.modules.certificate.criteria.entity.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CriteriaRepository extends JpaRepository<Criterion, UUID> {
    void deleteByCertificateId(UUID id);

    Criterion findCriteriaById(UUID id);

    @Query("""
        SELECT c
            FROM Criterion c
            WHERE c.certificate.id = :certificateId
    """)
    List<Criterion> findAllByCertificateId(@Param("certificateId") UUID certificateId);
}
