package com.annztech.rewardsystem.modules.certificate.management.service;

import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateCreateDTO;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateResponseDTO;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateUpdateDTO;

import java.util.List;

public interface CertificateService {
    CertificateIdResponseDTO createCertificate(CertificateCreateDTO dto);
    List<CertificateResponseDTO> getAllCertificates();
    CertificateResponseDTO getCertificateById(String id);
    CertificateIdResponseDTO updateCertificate(String id, CertificateUpdateDTO dto);
    CertificateIdResponseDTO deleteCertificate(String id);
}
