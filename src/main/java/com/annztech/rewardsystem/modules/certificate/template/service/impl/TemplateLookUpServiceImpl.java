package com.annztech.rewardsystem.modules.certificate.template.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.modules.certificate.template.entity.Template;
import com.annztech.rewardsystem.modules.certificate.template.repository.TemplateRepository;
import com.annztech.rewardsystem.modules.certificate.template.service.TemplateLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TemplateLookUpServiceImpl implements TemplateLookUpService {

    private final TemplateRepository templateRepository;

    public TemplateLookUpServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public Template getTemplateById(UUID id) {
        return templateRepository.findById(id).orElseThrow(() -> new AppException("Not found template", HttpStatus.NOT_FOUND));
    }

    @Override
    public Long getTemplateCount(Integer month, Integer year) {
        return templateRepository.countTemplatesByMonthYear(month, year);
    }
}
