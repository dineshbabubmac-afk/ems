package com.annztech.rewardsystem.modules.certificate.certificateemployee.service;

import com.annztech.rewardsystem.modules.certificate.certificateemployee.dto.CertificateEmployeeResponseDTO;

import java.util.List;

public interface CertificateEmployeeService {
     List<CertificateEmployeeResponseDTO> getAllMyRewards();
}
