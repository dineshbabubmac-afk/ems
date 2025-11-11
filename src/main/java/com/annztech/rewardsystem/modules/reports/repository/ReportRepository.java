package com.annztech.rewardsystem.modules.reports.repository;

import com.annztech.rewardsystem.modules.certificate.certificateemployee.entity.CertificateEmployee;
import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import com.annztech.rewardsystem.modules.reports.dto.ReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;


@Repository
public interface ReportRepository extends JpaRepository<CertificateRequest, Integer> {

//        @Query("""
//                SELECT com.annztech.rewardsystem.modules.reports.dto.response(
//                  TRIM(CONCAT(COALESCE(e.firstName, ''), ' ', COALESCE(e.lastName, ''))),
//                  cec.nameEnglish,
//                  e.Department.nameEn
//                )
//                FROM CertificateEmployee ce
//                JOIN Employee e ON ce.employee = e.id
//                JOIN Certificate c ON ce.certificate = c.id
//                JOIN CertificateCategory cec ON c.certificateCategory = cec.id
//                """)
//        Page<ReportDTO> fetchReport(@Param("certificateCategoryWise") String certificateCategoryWise,
//                                    @Param("departmentName") String departmentName,
//                                    @Param("rewardType") String rewardType,
//                                    @Param("approveType") String approveType,
//                                    @Param("startDate") String startDate,
//                                    @Param("endDate") String endDate,
//                                    Pageable pageable);

    @Query("""
    SELECT new com.annztech.rewardsystem.modules.reports.dto.ReportDTO(
        TRIM(CONCAT(COALESCE(e.firstName, ''), ' ', COALESCE(e.lastName, ''))),
        ce.certificate.certificateCategory.nameEnglish,
        e.department.nameEn
    )
    FROM CertificateEmployee ce
    LEFT JOIN Employee e ON ce.employee.id = e.id
    LEFT JOIN Certificate c ON ce.certificate.id = c.id
    LEFT JOIN CertificateCategory cc ON ce.certificate.certificateCategory.id = cc.id
    WHERE
    (:certificateCategoryId IS NULL OR ce.certificate.certificateCategory.id = :certificateCategoryId)
    AND (:departmentName IS NULL OR e.department.id = :departmentName) 
    AND ce.createdAt BETWEEN CAST(:startDate AS timestamp) AND CAST(:endDate AS timestamp)
""")

    Page<ReportDTO> fetchReport(
            @Param("certificateCategoryId") UUID certificateCategoryId,
            @Param("departmentName") UUID departmentName,
            @Param("rewardType") String rewardType,
            @Param("approveType") String approveType,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            Pageable pageable
    );


}


// AND ((:startDate IS NULL AND :endDate IS NULL) OR ce.createdAt BETWEEN :startDate AND :endDate)
//  AND (:rewardType IS NULL OR c.code = :rewardType)
//    AND (:approveType IS NULL OR ce.statusCode.code = :approveType)AND ((:startDate IS NULL AND :endDate IS NULL) OR ce.createdAt BETWEEN :startDate AND :endDate)
//

// WHERE
//          (:certificateCategoryWise IS NULL OR cec.id = :certificateCategoryWise)
//      AND (:departmentName IS NULL OR e.department.id = :departmentName)
//      AND (:rewardType IS NULL OR c.code = :rewardType)
//      AND (:approveType IS NULL OR ce.statusCode.code = :approveType)
//      AND ((:startDate IS NULL AND :endDate IS NULL) OR ce.createdAt BETWEEN :startDate AND :endDate)