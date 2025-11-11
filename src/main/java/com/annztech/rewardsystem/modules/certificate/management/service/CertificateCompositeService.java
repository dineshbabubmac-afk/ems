package com.annztech.rewardsystem.modules.certificate.management.service;

import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;
import com.annztech.rewardsystem.modules.certificate.template.entity.Template;

public interface CertificateCompositeService {
    CertificateCategory getCertificateCategoryById(String categoryId);
    Template getCertificateTemplateById(String templateId);
}
