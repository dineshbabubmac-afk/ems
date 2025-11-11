package com.annztech.rewardsystem.modules.lookups.service;

import com.annztech.rewardsystem.modules.certificate.management.entity.Certificate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CertificateLookUpService {
    Certificate getCertificateById(String id);
    Map<String, Long> getCertificatesCategoryWise(Integer month, Integer year);
    List<Certificate> getCertificateByTemplateId(UUID templateId);
}
