package com.annztech.rewardsystem.modules.certificate.category.service.impl;

import com.annztech.rewardsystem.modules.certificate.category.repository.CertificateCategoryRepository;
import com.annztech.rewardsystem.modules.certificate.category.service.CertificateCategoryLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CertificateCategoryLookUpServiceImpl implements CertificateCategoryLookUpService {

    private final CertificateCategoryRepository certificateCategoryRepository;

    public CertificateCategoryLookUpServiceImpl(CertificateCategoryRepository certificateCategoryRepository) {
        this.certificateCategoryRepository = certificateCategoryRepository;
    }

    @Override
    public Long getCertificateCategoriesCount(Integer month, Integer year) {
        return certificateCategoryRepository.countCertificateCategoriesByMonthYear(month, year);
    }
}
