package com.annztech.rewardsystem.modules.lookups.service.impl;

import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.management.entity.Certificate;
import com.annztech.rewardsystem.modules.certificate.management.repository.CertificateRepository;
import com.annztech.rewardsystem.modules.lookups.service.CertificateLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CertificateLookUpServiceImpl implements CertificateLookUpService {
    private final CertificateRepository certificateRepository;

    public CertificateLookUpServiceImpl(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Override
    public Certificate getCertificateById(String id) {
        log.info("Looking up Certificate by id: {}", id);
        StringsUtils.validateUUID(id, "Invalid Certificate id: " + id);
        UUID uuid = UUID.fromString(id);
        return certificateRepository.findById(uuid).orElseThrow( () -> new RuntimeException("Certificate Not Found") );
    }

    @Override
    public Map<String, Long> getCertificatesCategoryWise(Integer month, Integer year) {
        log.info("Fetching certificate counts by category...");
        List<Object[]> results = certificateRepository.findCertificatesCategoryWise(month, year);
        return results.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));
    }

    @Override
    public List<Certificate> getCertificateByTemplateId(UUID templateId) {
        return certificateRepository.findByCertificateTemplateId(templateId);
    }

}
