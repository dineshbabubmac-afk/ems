package com.annztech.rewardsystem.modules.certificate.request.repository;

import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequestCriterion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CertificateRequestCriterionRepository extends JpaRepository<CertificateRequestCriterion, UUID> {
    List<CertificateRequestCriterion> findCertificateRequestCriteriaByCertificateRequest_Id(UUID certificateRequestId);
}
