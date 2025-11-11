package com.annztech.rewardsystem.modules.certificate.management.repository;

import com.annztech.rewardsystem.modules.certificate.management.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    @Query("""
    SELECT cc.nameEnglish AS category, COUNT(c) AS count
    FROM Certificate c
    JOIN c.certificateCategory cc
    WHERE (:month IS NULL OR EXTRACT(MONTH FROM c.createdAt) = :month)
      AND (:year IS NULL OR EXTRACT(YEAR FROM c.createdAt) = :year)
      AND c.isDeleted = false
      AND c.isActive = true
    GROUP BY cc.nameEnglish
""")
    List<Object[]> findCertificatesCategoryWise(@Param("month") Integer month, @Param("year") Integer year);

    @Query("""
    SELECT c FROM Certificate c
    WHERE c.certificateTemplate.id = :templateId
      AND (c.isDeleted = false OR c.isDeleted IS NULL)
      AND (c.isActive = true OR c.isActive IS NULL)
""")
    List<Certificate> findByCertificateTemplateId(@Param("templateId") UUID templateId);

}
