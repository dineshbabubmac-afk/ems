package com.annztech.rewardsystem.modules.certificate.rejection.repository;

import com.annztech.rewardsystem.modules.certificate.rejection.entity.CertificateRequestRejection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CertificateRequestRejectionRepository extends JpaRepository<CertificateRequestRejection, UUID> {
}
