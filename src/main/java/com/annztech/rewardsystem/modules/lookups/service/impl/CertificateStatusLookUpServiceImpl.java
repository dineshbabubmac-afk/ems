package com.annztech.rewardsystem.modules.lookups.service.impl;

import com.annztech.rewardsystem.modules.certificate.status.entity.CertificateStatus;
import com.annztech.rewardsystem.modules.certificate.status.repository.CertificateStatusRepository;
import com.annztech.rewardsystem.modules.lookups.service.CertificateStatusLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CertificateStatusLookUpServiceImpl implements CertificateStatusLookUpService {
    private final CertificateStatusRepository certificateStatusRepository;

    public CertificateStatusLookUpServiceImpl(CertificateStatusRepository certificateStatusRepository) {
        this.certificateStatusRepository = certificateStatusRepository;
    }

    @Override
    public CertificateStatus getCertificateStatusCode(String certificateStaus) {
        log.info("Looking up Certificate by status: {}", certificateStaus);
        return certificateStatusRepository.findCertificateStatusByCode(certificateStaus).orElseThrow(() -> new RuntimeException("Certificate Status Code Not Found"));
    }
}
