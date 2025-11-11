package com.annztech.rewardsystem.modules.certificate.status.repository;

import com.annztech.rewardsystem.modules.certificate.status.entity.CertificateStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface CertificateStatusRepository extends JpaRepository<CertificateStatus, UUID> {
    Optional<CertificateStatus> findCertificateStatusByCode(String statusCode);
}
