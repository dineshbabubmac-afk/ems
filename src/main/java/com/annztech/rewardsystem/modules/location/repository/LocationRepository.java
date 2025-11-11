package com.annztech.rewardsystem.modules.location.repository;

import com.annztech.rewardsystem.modules.location.entity.Location;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.nameEn != 'SUPER_ADMIN'")
    List<Location> findAll();
    boolean existsLocationByNameEn(String portalOwner);
    Optional<Location> findLocationByNameEn(String locationName);
    boolean existsLocationByNameAr(String nameAr);
    @Query("SELECT l FROM Location l WHERE " +
            "LOWER(l.nameEn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(l.nameAr) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Location> searchByKeyword(@Param("keyword") String keyword);
    @Query("SELECT l FROM Location l WHERE l.nameEn != 'SUPER_ADMIN'")
    List<Location> findAllLocations(Sort orders);

    @Query("""
        SELECT COUNT(l)
        FROM Location l
        WHERE l.isDeleted = false
          AND l.isActive = true
          AND (:month IS NULL OR EXTRACT(MONTH FROM l.createdAt) = :month)
          AND (:year IS NULL OR EXTRACT(YEAR FROM l.createdAt) = :year)
    """)
    Long countLocationsByMonthYear(@Param("month") Integer month, @Param("year") Integer year);
}
