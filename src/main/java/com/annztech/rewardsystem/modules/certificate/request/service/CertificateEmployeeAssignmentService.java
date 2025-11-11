package com.annztech.rewardsystem.modules.certificate.request.service;

import com.annztech.rewardsystem.modules.certificate.certificateemployee.dto.CertificateEmployeeResponseDTO;
import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;

public interface CertificateEmployeeAssignmentService {
    void publishCertificate(CertificateRequest certificateRequest);
}
