package com.annztech.rewardsystem.modules.certificate.management.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.repsoitory.SequenceGeneratorRepository;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;
import com.annztech.rewardsystem.modules.certificate.criteria.entity.Criterion;
import com.annztech.rewardsystem.modules.certificate.criteria.repository.CriteriaRepository;
import com.annztech.rewardsystem.modules.certificate.management.constants.CertificateConstants;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateCreateDTO;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateResponseDTO;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.management.entity.Certificate;
import com.annztech.rewardsystem.modules.certificate.management.mapper.CertificateMapper;
import com.annztech.rewardsystem.modules.certificate.management.repository.CertificateRepository;
import com.annztech.rewardsystem.modules.certificate.management.service.CertificateCompositeService;
import com.annztech.rewardsystem.modules.certificate.management.service.CertificateService;
import com.annztech.rewardsystem.modules.certificate.template.entity.Template;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CertificateServiceImpl extends LocalizationService implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final CertificateMapper certificateMapper;
    private final SequenceGeneratorRepository sequenceGenerator;
    private final CriteriaRepository criteriaRepository;
    private final CertificateCompositeService certificateCompositeService;

    public CertificateServiceImpl(CertificateRepository certificateRepository, CertificateMapper certificateMapper, SequenceGeneratorRepository sequenceGenerator, CriteriaRepository criteriaRepository, CertificateCompositeService certificateCompositeService) {
        this.certificateRepository = certificateRepository;
        this.certificateMapper = certificateMapper;
        this.sequenceGenerator = sequenceGenerator;
        this.criteriaRepository = criteriaRepository;
        this.certificateCompositeService = certificateCompositeService;
    }

    @Override
    @Transactional
    public CertificateIdResponseDTO createCertificate(CertificateCreateDTO dto) {
        Certificate certificate = certificateMapper.toEntity(dto);
        certificate.setCode(getSequenceId());

        CertificateCategory certificateCategory = certificateCompositeService.getCertificateCategoryById(dto.getCategoryId());
        certificate.setCertificateCategory(certificateCategory);

        Template template = certificateCompositeService.getCertificateTemplateById(dto.getTemplateId());
        certificate.setCertificateTemplate(template);

        Certificate savedEntity = certificateRepository.save(certificate);
        if (dto.getCriteriaList() != null && !dto.getCriteriaList().isEmpty()) {
            certificate.setCriteriaRequired(true);
            List<Criterion> criteria = certificateMapper.toCriterionEntityList(dto.getCriteriaList());
            criteria.forEach(c -> c.setCertificate(savedEntity));
            criteriaRepository.saveAll(criteria);
        }
        return new CertificateIdResponseDTO(savedEntity.getId().toString());
    }

    @Override
    public List<CertificateResponseDTO> getAllCertificates() {
        List<Certificate> certificates = certificateRepository.findAll(DomainHelper.sortByCreatedAtDesc());
        return certificates.stream()
                .map(cert -> {
                    List<Criterion> criteriaList = criteriaRepository.findAllByCertificateId(cert.getId());
                    return new CertificateResponseDTO(cert, criteriaList);
                })
                .toList();
    }

    @Override
    public CertificateResponseDTO getCertificateById(String id) {
        Certificate certificate = validateAndGetCertificate(id);
        List<Criterion> criteriaList = criteriaRepository.findAllByCertificateId(certificate.getId());
        return new CertificateResponseDTO(certificate, criteriaList);
    }

    @Override
    @Transactional
    public CertificateIdResponseDTO updateCertificate(String id, CertificateUpdateDTO dto) {
        Certificate certificate = validateAndGetCertificate(id);
        if (StringsUtils.isNotBlank(dto.getCategoryId())) {
            CertificateCategory certificateCategory = certificateCompositeService.getCertificateCategoryById(dto.getCategoryId());
            certificate.setCertificateCategory(certificateCategory);
        }
        if (StringsUtils.isNotBlank(dto.getTemplateId())) {
            Template template = certificateCompositeService.getCertificateTemplateById(dto.getTemplateId());
            certificate.setCertificateTemplate(template);
        }
        certificateMapper.updateEntityFromDto(dto, certificate);
        Certificate updated = certificateRepository.save(certificate);
        if(dto.getCriteriaList() != null){
            criteriaRepository.deleteByCertificateId(updated.getId());
            List<Criterion> newCriteria = certificateMapper.toCriterionEntityList(dto.getCriteriaList());
            newCriteria.forEach(c -> c.setCertificate(updated));
            criteriaRepository.saveAll(newCriteria);
        }
        return new CertificateIdResponseDTO(updated.getId().toString());
    }

    @Override
    public CertificateIdResponseDTO deleteCertificate(String id) {
        Certificate certificate = validateAndGetCertificate(id);
        certificate.setIsDeleted(true);
        certificate.setIsActive(false);
        certificateRepository.save(certificate);
        return new CertificateIdResponseDTO(certificate.getId().toString());
    }

    private Certificate validateAndGetCertificate(String id) {
        StringsUtils.validateUUID(id, getMessage(CertificateConstants.INVALID_CERTIFICATE_ID));
        return certificateRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(getMessage(CertificateConstants.CERTIFICATE_NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    private String getSequenceId(){
        Long nextVal = sequenceGenerator.getNextSequence(CertificateConstants.CERTIFICATE_CODE_SEQ);
        return String.format(CertificateConstants.CERTIFICATE_CODE_FORMAT, nextVal);
    }
}
