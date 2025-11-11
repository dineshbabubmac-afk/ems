package com.annztech.rewardsystem.modules.employee.repository;

import com.annztech.rewardsystem.modules.employee.entity.Employee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    @EntityGraph(attributePaths = {"department", "location", "job"})
    Optional<Employee> findById(UUID id);

    @Query("SELECT e FROM Employee e WHERE e.email = :email")
    Optional<Employee> findByEmail(@Param("email") String email);

    boolean existsEmployeeByEmail(String portalOwner);

    Employee findEmployeesByEmail(String email);

    Employee findEmployeeById(UUID id);

    List<Employee> findEmployeeByDepartment_Id(UUID departmentId);

    @Query("""
    SELECT e FROM Employee e
    WHERE (:query IS NULL OR :query = '' OR
          LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR
          LOWER(e.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR
          LOWER(e.email) LIKE LOWER(CONCAT('%', :query, '%')))
""")
    List<Employee> searchEmployees(@Param("query") String query);


    @Query("SELECT e FROM Employee e WHERE e.isDeleted = false AND e.isActive = true")
    List<Employee> findAllActiveEmployees(Sort sort);

    @Query("SELECT e FROM Employee e WHERE e.isDeleted = true AND e.isActive = false ")
    List<Employee> findAllInActiveEmployees(Sort sort);

    Optional<List<Employee>> findEmployeesByLocation_Id(Long locationId);

    List<Employee> findEmployeeByJob_Id(UUID jobId);

    @Query("SELECT e FROM Employee e " +
            "WHERE (:query IS NULL OR :query = '' OR "+
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(e.email) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND e.role.code = :roleCode")
    List<Employee> searchEmployee(@Param("query") String query, @Param("roleCode") String roleCode);

    @Query("SELECT e FROM Employee e " +
            "WHERE (:query IS NULL OR :query = '' OR " +
            "LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(e.department.nameEn) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(e.department.nameAr) LIKE LOWER(CONCAT('%', :query, '%'))) "+
            "AND e.role.code = :roleCode")
    List<Employee> searchEmployeeByDepartmentOrEmployeeId(@Param("query") String query, @Param("roleCode") String roleCode);


    @Query("SELECT e FROM Employee e WHERE e.role.id IN :roleIds")
    List<Employee> findByRoleIds(@Param("roleIds") List<UUID> roleIds, Sort sort);

    List<Employee> findAllByRole_Code(String code);
    @Query("""
    SELECT d.nameEn AS department, COUNT(e) AS count
    FROM Employee e
    JOIN e.department d
    WHERE (:month IS NULL OR EXTRACT(MONTH FROM e.createdAt) = :month)
      AND (:year IS NULL OR EXTRACT(YEAR FROM e.createdAt) = :year)
      AND e.isDeleted = false
      AND e.isActive = true
    GROUP BY d.nameEn
""")
    List<Object[]> findEmployeeCountByDepartment(@Param("month") Integer month, @Param("year") Integer year);

    @Query("""
    SELECT COUNT(e)
    FROM Employee e
    WHERE e.isDeleted = false
      AND e.isActive = true
      AND (:month IS NULL OR EXTRACT(MONTH FROM e.createdAt) = :month)
      AND (:year IS NULL OR EXTRACT(YEAR FROM e.createdAt) = :year)
""")
    Long getActiveEmployeesCount(@Param("month") Integer month, @Param("year") Integer year);
}
