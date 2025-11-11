package com.annztech.rewardsystem.modules.lookups.service;

import com.annztech.rewardsystem.modules.certificate.status.entity.CertificateStatus;

public interface CertificateStatusLookUpService {
    CertificateStatus getCertificateStatusCode(String certificateStaus);
}
