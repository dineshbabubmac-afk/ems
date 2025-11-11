package com.annztech.rewardsystem.modules.certificate.certificateemployee.repository;

import com.annztech.rewardsystem.modules.certificate.certificateemployee.entity.CertificateEmployee;
import com.annztech.rewardsystem.modules.metrics.dto.TopEmployeesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificateEmployeeRepository extends JpaRepository<CertificateEmployee, UUID> {


    @Query("""
    SELECT new com.annztech.rewardsystem.modules.metrics.dto.TopEmployeesDTO(
        CONCAT(e.firstName, ' ', COALESCE(e.lastName, '')),
        d.nameEn,
        CAST(COUNT(ce) AS int),
        e.profilePicturePath
    )
    FROM CertificateEmployee ce
    JOIN ce.employee e
    JOIN e.department d
    WHERE (:month IS NULL OR EXTRACT(MONTH FROM ce.createdAt) = :month)
      AND (:year IS NULL OR EXTRACT(YEAR FROM ce.createdAt) = :year)
      AND e.isDeleted = false
      AND e.isActive = true
    GROUP BY e.id, e.firstName, e.lastName, d.nameEn, e.profilePicturePath
    ORDER BY COUNT(ce) DESC
""")
    List<TopEmployeesDTO> findTopEmployees(@Param("month") Integer month, @Param("year") Integer year);


    @Query("""
    SELECT d.nameEn, COUNT(ce)
    FROM CertificateEmployee ce
    JOIN ce.employee e
    JOIN e.department d
    WHERE (:month IS NULL OR EXTRACT(MONTH FROM ce.createdAt) = :month)
      AND (:year IS NULL OR EXTRACT(YEAR FROM ce.createdAt) = :year)
      AND e.isDeleted = false
      AND e.isActive = true
    GROUP BY d.nameEn
    ORDER BY COUNT(ce) DESC
""")
    List<Object[]> findTopDepartments(@Param("month") Integer month, @Param("year") Integer year);

    @Query("""
    SELECT COUNT(ce)
    FROM CertificateEmployee ce
    WHERE ce.employee.isDeleted = false
      AND ce.employee.isActive = true
      AND (:month IS NULL OR EXTRACT(MONTH FROM ce.createdAt) = :month)
      AND (:year IS NULL OR EXTRACT(YEAR FROM ce.createdAt) = :year)
""")
    Long getCertificatesCount(@Param("month") Integer month, @Param("year") Integer year);

    @Query("""
        SELECT ce
        FROM CertificateEmployee ce
        JOIN FETCH ce.certificate c
        JOIN FETCH ce.employee e
        JOIN FETCH c.certificateCategory cc
        WHERE LOWER(e.email) = LOWER(:email)
        ORDER BY ce.createdAt DESC
    """)
    List<CertificateEmployee> findAllByEmployeeEmail(@Param("email") String email);

    List<CertificateEmployee> findAllByEmployee_Id(UUID employeeId);

    @Query("""
    SELECT ce FROM CertificateEmployee ce
    WHERE ce.employee.id = :employeeId
      AND ce.certificate.id IN :certificateIds
    ORDER BY ce.createdAt DESC
""")
    Optional<CertificateEmployee> findFirstByEmployeeIdAndCertificateIdIn(
            @Param("employeeId") UUID employeeId,
            @Param("certificateIds") List<UUID> certificateIds
    );
}
