package com.annztech.rewardsystem.modules.certificate.template.service;

import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateCreateDTO;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateResponseDTO;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.template.entity.Template;

import java.util.List;

public interface TemplateService {
    TemplateIdResponseDTO createCertificateTemplate(TemplateCreateDTO dto);
    List<TemplateResponseDTO> getAllCertificateTemplates();
    TemplateResponseDTO getCertificateTemplateById(String id);
    TemplateIdResponseDTO updateCertificateTemplate(String id, TemplateUpdateDTO dto);
    TemplateIdResponseDTO deleteCertificateTemplate(String id);
    String getCertificateTemplateViewById(String id);

    Template getTemplateById(String templateId);
}
