package com.annztech.rewardsystem.modules.job.repository;

import com.annztech.rewardsystem.modules.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    @Query("SELECT j FROM Job j WHERE j.titleEn != 'SUPER_ADMIN'")
    List<Job> findAll();

    void deleteJobById(UUID id);

    boolean existsJobByTitleEn(String portalOwner);

    Optional<Job> findJobByTitleEn(String jobTitle);

    boolean existsJobByTitleAr(String titleAr);

    @Query("SELECT j FROM Job j " +
            "WHERE LOWER(j.titleEn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.titleAr) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Job> searchByKeyword(@Param("keyword") String keyword);

    Job findJobById(UUID jobUuid);

    @Query("""
        SELECT COUNT(j)
        FROM Job j
        WHERE j.isDeleted = false
          AND j.isActive = true
          AND (:month IS NULL OR EXTRACT(MONTH FROM j.createdAt) = :month)
          AND (:year IS NULL OR EXTRACT(YEAR FROM j.createdAt) = :year)
    """)
    Long countJobsByMonthYear(@Param("month") Integer month, @Param("year") Integer year);
}
