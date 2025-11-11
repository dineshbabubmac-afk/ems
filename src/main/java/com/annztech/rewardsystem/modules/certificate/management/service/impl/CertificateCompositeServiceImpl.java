package com.annztech.rewardsystem.modules.certificate.management.service.impl;

import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;
import com.annztech.rewardsystem.modules.certificate.category.service.CertificateCategoryService;
import com.annztech.rewardsystem.modules.certificate.management.service.CertificateCompositeService;
import com.annztech.rewardsystem.modules.certificate.template.entity.Template;
import com.annztech.rewardsystem.modules.certificate.template.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CertificateCompositeServiceImpl extends LocalizationService implements CertificateCompositeService {
    private final CertificateCategoryService certificateCategoryService;
    private final TemplateService templateService;

    public CertificateCompositeServiceImpl(CertificateCategoryService certificateCategoryService, TemplateService templateService) {
        this.certificateCategoryService = certificateCategoryService;
        this.templateService = templateService;
    }

    @Override
    public CertificateCategory getCertificateCategoryById(String categoryId) {
        return certificateCategoryService.getCertificateCategoryById(categoryId);
    }

    @Override
    public Template getCertificateTemplateById(String templateId) {
        return templateService.getTemplateById(templateId);
    }
}
