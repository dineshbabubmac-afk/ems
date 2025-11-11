package com.annztech.rewardsystem.modules.certificate.request.repository;

import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificateRequestRepository extends JpaRepository<CertificateRequest, UUID> {
    @Query("""
        SELECT cr
        FROM CertificateRequest cr
        WHERE
            (:query IS NULL OR LOWER(cr.code) LIKE LOWER(CONCAT('%', :query, '%'))
                 OR LOWER(cr.nominatedTo.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
                 OR LOWER(cr.nominatedBy.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            AND (:action IS NULL OR cr.isActive = :action)
            AND (:statusCode IS NULL OR cr.statusCode.code = :statusCode)
    """)
    List<CertificateRequest> search(@Param("query") String query, @Param("action") Boolean action, @Param("statusCode") String statusCode);

    @Query("""
    SELECT cr.statusCode.code, COUNT(cr)
    FROM CertificateRequest cr
    WHERE (:month IS NULL OR EXTRACT(MONTH FROM cr.createdAt) = :month)
      AND (:year IS NULL OR EXTRACT(YEAR FROM cr.createdAt) = :year)
    GROUP BY cr.statusCode.code
""")
    List<Object[]> findNominationStatusCount(@Param("month") Integer month, @Param("year") Integer year);

    Optional<CertificateRequest> findByCertificate_Id(UUID certificateId);

    Page<CertificateRequest> findAllByStatusCode_Code(@Param("code") String code, Pageable pageable);
}
