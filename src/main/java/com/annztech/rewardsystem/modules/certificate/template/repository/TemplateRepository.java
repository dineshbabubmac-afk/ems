package com.annztech.rewardsystem.modules.certificate.template.repository;

import com.annztech.rewardsystem.modules.certificate.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, UUID> {
    Optional<Template> findByCode(String code);

    @Query("""
        SELECT COUNT(t)
        FROM Template t
        WHERE t.isDeleted = false
          AND t.isActive = true
          AND (:month IS NULL OR EXTRACT(MONTH FROM t.createdAt) = :month)
          AND (:year IS NULL OR EXTRACT(YEAR FROM t.createdAt) = :year)
    """)
    Long countTemplatesByMonthYear(@Param("month") Integer month, @Param("year") Integer year);
}
