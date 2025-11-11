package com.annztech.rewardsystem.modules.department.repository;

import com.annztech.rewardsystem.modules.department.entity.Department;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    @Query("SELECT d FROM Department d WHERE d.nameEn != 'SUPER_ADMIN' ORDER BY d.updatedAt DESC")
    List<Department> findAll();

    boolean existsDepartmentByNameEn(String nameEn);

    Optional<Department> findDepartmentByNameEn(String department);
    boolean existsDepartmentByNameAr(String nameAr);

    @Query("""
    SELECT d FROM Department d
    WHERE (:keyword IS NULL OR :keyword = '' OR 
          LOWER(d.nameEn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
          LOWER(d.nameAr) LIKE LOWER(CONCAT('%', :keyword, '%')))
""")
    List<Department> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT d FROM Department d WHERE d.nameEn != 'SUPER_ADMIN'")
    List<Department> findAllDepartments(Sort orders);

    Department findDepartmentById(UUID id);

    @Query("""
    SELECT COUNT(d)
    FROM Department d
    WHERE d.isDeleted = false
      AND d.isActive = true
      AND (:month IS NULL OR EXTRACT(MONTH FROM d.createdAt) = :month)
      AND (:year IS NULL OR EXTRACT(YEAR FROM d.createdAt) = :year)
""")
    Long getOverAllDepartmentsCount(@Param("month") Integer month, @Param("year") Integer year);
}
