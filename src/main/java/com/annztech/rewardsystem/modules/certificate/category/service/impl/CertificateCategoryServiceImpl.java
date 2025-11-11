package com.annztech.rewardsystem.modules.certificate.category.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.category.constants.CertificateCategoryConstants;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryCreateDTO;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryResponseDTO;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;
import com.annztech.rewardsystem.modules.certificate.category.mapper.CertificateCategoryMapper;
import com.annztech.rewardsystem.modules.certificate.category.repository.CertificateCategoryRepository;
import com.annztech.rewardsystem.modules.certificate.category.service.CertificateCategoryService;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CertificateCategoryServiceImpl extends LocalizationService implements CertificateCategoryService {
    private final CertificateCategoryRepository certificateCategoryRepository;
    private final CertificateCategoryMapper certificateCategoryMapper;

    public CertificateCategoryServiceImpl(CertificateCategoryRepository certificateCategoryRepository, CertificateCategoryMapper certificateCategoryMapper) {
        this.certificateCategoryRepository = certificateCategoryRepository;
        this.certificateCategoryMapper = certificateCategoryMapper;
    }

    @Override
    public CertificateCategoryIdResponseDTO createCertificateCategory(CertificateCategoryCreateDTO dto) {
        CertificateCategory certificateCategory = certificateCategoryMapper.toEntity(dto);
        certificateCategory.setIsActive(true);
        certificateCategory.setIsDeleted(false);
        CertificateCategory savedCategory = certificateCategoryRepository.save(certificateCategory);
        return new CertificateCategoryIdResponseDTO(savedCategory.getId().toString());
    }

    @Override
    public List<CertificateCategoryResponseDTO> getAllCertificateCategories() {
        List<CertificateCategory> categories = certificateCategoryRepository.findAll(DomainHelper.sortByCreatedAtDesc());
        return categories.stream().map(CertificateCategoryResponseDTO::new).toList();
    }

    @Override
    public CertificateCategoryIdResponseDTO updateCertificateCategory(String certificateCategoryId, CertificateCategoryUpdateDTO dto) {
        CertificateCategory certificateCategory = validateAndGetCertificateCategory(certificateCategoryId);
        certificateCategoryMapper.updateEntityFromDto(dto, certificateCategory);
        CertificateCategory updatedCategory = certificateCategoryRepository.save(certificateCategory);
        return new CertificateCategoryIdResponseDTO(updatedCategory.getId().toString());
    }

    @Override
    public CertificateCategoryIdResponseDTO deleteCertificateCategory(String id) {
        CertificateCategory certificateCategory = validateAndGetCertificateCategory(id);
        certificateCategory.setIsDeleted(true);
        certificateCategory.setIsActive(false);
        certificateCategoryRepository.save(certificateCategory);
        return new CertificateCategoryIdResponseDTO(certificateCategory.getId().toString());
    }

    @Override
    public CertificateCategory getCertificateCategoryById(String categoryId) {
        return certificateCategoryRepository.findById(UUID.fromString(categoryId))
                .orElseThrow(() -> new AppException(getMessage(CertificateCategoryConstants.CERTIFICATE_CATEGORY_NO_RECORDS), HttpStatus.NOT_FOUND));
    }

    private CertificateCategory validateAndGetCertificateCategory(String id) {
        StringsUtils.validateUUID(id, getMessage(CertificateCategoryConstants.CERTIFICATE_CATEGORY_INVALID_ID));
        return certificateCategoryRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(getMessage(CertificateCategoryConstants.CERTIFICATE_CATEGORY_NO_RECORDS), HttpStatus.NOT_FOUND));
    }
}
