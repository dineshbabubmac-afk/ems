package com.annztech.rewardsystem.modules.certificate.template.service;

import com.annztech.rewardsystem.modules.certificate.template.entity.Template;

import java.util.UUID;

public interface TemplateLookUpService {
    Template getTemplateById(UUID id);
    Long getTemplateCount(Integer month, Integer year);
}
