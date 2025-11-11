package com.annztech.rewardsystem.modules.certificate.category.service;

import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryCreateDTO;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryResponseDTO;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;

import java.util.List;

public interface CertificateCategoryService {

    CertificateCategoryIdResponseDTO createCertificateCategory(CertificateCategoryCreateDTO dto);
    List<CertificateCategoryResponseDTO> getAllCertificateCategories();
    CertificateCategoryIdResponseDTO updateCertificateCategory(String certificateCategoryId, CertificateCategoryUpdateDTO dto);
    CertificateCategoryIdResponseDTO deleteCertificateCategory(String id);

    CertificateCategory getCertificateCategoryById(String categoryId);

}
