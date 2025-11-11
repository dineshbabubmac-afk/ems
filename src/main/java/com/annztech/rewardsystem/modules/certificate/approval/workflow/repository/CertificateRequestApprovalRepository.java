package com.annztech.rewardsystem.modules.certificate.approval.workflow.repository;

import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateApprovalInfoDTO;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.entity.CertificateRequestApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificateRequestApprovalRepository extends JpaRepository<CertificateRequestApproval, UUID> {
    List<CertificateRequestApproval> findAllByMember_IdAndStatusCode_Code(UUID memberId, String statusCode);
//    @Query("""
//        SELECT cra FROM CertificateRequestApproval cra
//        WHERE (:statusCode IS NULL OR cra.certificateRequest.statusCode.code = :statusCode)
//          AND (:isActive IS NULL OR cra.certificateRequest.isActive = :isActive)
//    """)
//    Page<CertificateRequestApproval> findAllByStatusCode_Code(String statusCode, String isActive, Pageable pageable);
//    @Query("""
//    SELECT cr FROM CertificateRequestApproval cr
//    WHERE LOWER(cr.member.department.nameEn) LIKE LOWER(CONCAT('%', :query, '%'))
//       OR LOWER(cr.member.department.nameAr) LIKE LOWER(CONCAT('%', :query, '%'))
//       OR LOWER(cr.certificateRequest.nominatedBy.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
//       OR LOWER(cr.certificateRequest.certificate.certificateCategory.nameEnglish) LIKE LOWER(CONCAT('%', :query, '%'))
//       OR LOWER(cr.certificateRequest.certificate.certificateCategory.nameArabic) LIKE LOWER(CONCAT('%', :query, '%'))
//    """)
//    List<CertificateRequestApproval> findAllBySearch(@Param("query") String query);

    @Query("""
    SELECT cr FROM CertificateRequestApproval cr
    WHERE
        (:statusCode IS NULL OR cr.statusCode.code = :statusCode)
        AND (:isActive IS NULL OR cr.certificateRequest.isActive = :isActive)
        AND (
            :query IS NULL OR (
                LOWER(cr.member.department.nameEn) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(cr.member.department.nameAr) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(cr.certificateRequest.nominatedBy.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(cr.certificateRequest.certificate.certificateCategory.nameEnglish) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(cr.certificateRequest.certificate.certificateCategory.nameArabic) LIKE LOWER(CONCAT('%', :query, '%'))
            )
        )
""")
    Page<CertificateRequestApproval> findAllFiltered(
            @Param("statusCode") String statusCode,
            @Param("isActive") Boolean isActive,
            @Param("query") String query,
            Pageable pageable
    );

    @Query("""
    SELECT cr FROM CertificateRequestApproval cr
    WHERE
        (:statusCode IS NULL OR cr.statusCode.code = :statusCode)
        AND (:isActive IS NULL OR cr.certificateRequest.isActive = :isActive)
        AND (
            :query IS NULL OR (
                LOWER(cr.member.department.nameEn) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(cr.member.department.nameAr) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(cr.certificateRequest.nominatedBy.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(cr.certificateRequest.certificate.certificateCategory.nameEnglish) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(cr.certificateRequest.certificate.certificateCategory.nameArabic) LIKE LOWER(CONCAT('%', :query, '%'))
            )
        )
        AND cr.member.id = :memberId
""")
    Page<CertificateRequestApproval> findAllCertificateRequest(
            @Param("statusCode") String statusCode,
            @Param("isActive") Boolean isActive,
            @Param("query") String query,
            @Param("memberId") UUID memberId,
            Pageable pageable
    );

    @Query("""
    SELECT new com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateApprovalInfoDTO(
        cra.certificateRequest.id,
        m.id,
        m.firstName,
        m.lastName,
        cra.statusCode.code,
        cra.reason,
        cam.memberRoleCode.code,
        cra.updatedAt
    )
    FROM CertificateRequestApproval cra
    JOIN cra.member m
    JOIN CertificateCommittee cam ON cam.member = m
    WHERE cra.certificateRequest.id = :certificateRequestId
    ORDER BY cra.updatedAt DESC
""")
    List<CertificateApprovalInfoDTO> findCertificateApprovalDetailsByRequestId(
            @Param("certificateRequestId") UUID certificateRequestId);


    @Query("""
    SELECT new com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateApprovalInfoDTO(
        cra.certificateRequest.id,
        m.id,
        m.firstName,
        m.lastName,
        cra.statusCode.code,
        cra.reason,
        cam.memberRoleCode.code,
        cra.updatedAt
    )
    FROM CertificateRequestApproval cra
    JOIN cra.member m
    JOIN CertificateCommittee cam ON cam.member = m
    WHERE cra.certificateRequest.id = :certificateRequestId
      AND m.id = :memberId
    ORDER BY cra.updatedAt DESC
""")
    List<CertificateApprovalInfoDTO> findCertificateApprovalDetailsByRequestIdAndMemberId(
            @Param("certificateRequestId") UUID certificateRequestId,
            @Param("memberId") UUID memberId);


}
