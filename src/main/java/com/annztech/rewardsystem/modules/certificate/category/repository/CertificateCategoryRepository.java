package com.annztech.rewardsystem.modules.certificate.category.repository;

import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CertificateCategoryRepository extends JpaRepository<CertificateCategory, UUID> {

    @Query("""
        SELECT COUNT(cc)
        FROM CertificateCategory cc
        WHERE cc.isDeleted = false
          AND cc.isActive = true
          AND (:month IS NULL OR EXTRACT(MONTH FROM cc.createdAt) = :month)
          AND (:year IS NULL OR EXTRACT(YEAR FROM cc.createdAt) = :year)
    """)
    Long countCertificateCategoriesByMonthYear(@Param("month") Integer month, @Param("year") Integer year);
}
