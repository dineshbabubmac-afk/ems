package com.annztech.rewardsystem.modules.certificate.template.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.repsoitory.SequenceGeneratorRepository;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.template.constants.TemplateConstants;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateCreateDTO;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateResponseDTO;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.template.entity.Template;
import com.annztech.rewardsystem.modules.certificate.template.helper.TemplateHelper;
import com.annztech.rewardsystem.modules.certificate.template.mapper.TemplateMapper;
import com.annztech.rewardsystem.modules.certificate.template.repository.TemplateRepository;
import com.annztech.rewardsystem.modules.certificate.template.service.TemplateService;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import com.annztech.rewardsystem.modules.tinyurl.dto.TinyUrlDTO;
import com.annztech.rewardsystem.modules.tinyurl.service.TinyUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class TemplateServiceImpl extends LocalizationService implements TemplateService {
    private final TemplateRepository templateRepository;
    private final TemplateMapper templateMapper;
    private final TinyUrlService tinyUrlService;

    private final SequenceGeneratorRepository sequenceGenerator;
    public TemplateServiceImpl(TemplateRepository templateRepository, TemplateMapper templateMapper, TinyUrlService tinyUrlService, SequenceGeneratorRepository sequenceGenerator) {
        this.templateRepository = templateRepository;
        this.templateMapper = templateMapper;
        this.tinyUrlService = tinyUrlService;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public TemplateIdResponseDTO createCertificateTemplate(TemplateCreateDTO dto) {
        Template template = templateMapper.toEntity(dto);
        template.setCode(getSequenceId());
        Template saved = templateRepository.save(template);
        return new TemplateIdResponseDTO(saved.getId().toString());
    }

    @Override
    public List<TemplateResponseDTO> getAllCertificateTemplates() {
        List<Template> templates = templateRepository.findAll(DomainHelper.sortByCreatedAtDesc());
        return templates.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public TemplateResponseDTO getCertificateTemplateById(String id) {
        Template template = getTemplateById(id);
        return mapToResponseDTO(template);
    }

    @Override
    public TemplateIdResponseDTO updateCertificateTemplate(String id, TemplateUpdateDTO dto) {
        Template existing = getTemplateById(id);
        templateMapper.updateEntityFromDto(dto, existing);
        Template updated = templateRepository.save(existing);
        return new TemplateIdResponseDTO(updated.getId().toString());
    }

    @Override
    public TemplateIdResponseDTO deleteCertificateTemplate(String id) {
        Template existing = getTemplateById(id);
        existing.setIsActive(false);
        existing.setIsDeleted(true);
        templateRepository.save(existing);
        return new TemplateIdResponseDTO(existing.getId().toString());
    }

    @Override
    public String getCertificateTemplateViewById(String id) {
        try {
            Template template = getTemplateById(id);
            String logo = TemplateConstants.LOGO;
            String employeeName = TemplateConstants.NAME;
            String certificateName = template.getName();
            String empCode = TemplateConstants.EMP_CODE;
            String issueDate = DateTimeFormatter.ofPattern("dd MMM yyyy").withZone(ZoneOffset.UTC).format(template.getCreatedAt());
            Map<String, String> variables = Map.of(
                    "logo", logo,
                    "name", employeeName != null ? employeeName : "",
                    "certificateName", certificateName != null ? certificateName : "",
                    "date", issueDate,
                    "empCode", empCode
            );
            String renderedHtml = TemplateHelper.loadCertificateTemplate("templates/" + template.getPath() + ".html", variables);
            return renderedHtml;
        } catch (Exception e) {
            log.error("Error rendering certificate template [{}]: {}", id, e.getMessage());
            throw new AppException("Failed to render certificate template", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Template getTemplateById(String templateId) {
        StringsUtils.validateUUID(templateId, getMessage(TemplateConstants.INVALID_TEMPLATE_ID));
        return templateRepository.findById(UUID.fromString(templateId))
                .orElseThrow(() -> new AppException(getMessage(TemplateConstants.TEMPLATE_NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    private TemplateResponseDTO mapToResponseDTO(Template template) {
        TemplateResponseDTO dto = new TemplateResponseDTO(template);
        try {
            String longUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/certificate-template/")
                    .path(template.getId().toString())
                    .path("/view")
                    .toUriString();
            TinyUrlDTO urlDTO = tinyUrlService.createTinyUrl(longUrl);
            dto.setTemplateContent(urlDTO.getTinyUrl());
        } catch (Exception e) {
            log.error("Error generating tiny URL for template {}", template.getId(), e);
            dto.setTemplateContent(null);
        }
        return dto;
    }

    private String getSequenceId(){
        Long nextVal = sequenceGenerator.getNextSequence(TemplateConstants.TEMPLATE_CODE_SEQ);
        return String.format(TemplateConstants.TEMPLATE_CODE_FORMAT, nextVal);
    }
}
