package com.annztech.rewardsystem.modules.certificate.request.service.impl;

import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.dto.CertificateEmployeeResponseDTO;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.entity.CertificateEmployee;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.repository.CertificateEmployeeRepository;
import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import com.annztech.rewardsystem.modules.certificate.request.service.CertificateEmployeeAssignmentService;
import org.springframework.stereotype.Service;

@Service
public class CertificateEmployeeAssignmentServiceImpl extends LocalizationService implements CertificateEmployeeAssignmentService {
    private final CertificateEmployeeRepository certificateEmployeeRepository;

    public CertificateEmployeeAssignmentServiceImpl(CertificateEmployeeRepository certificateEmployeeRepository) {
        this.certificateEmployeeRepository = certificateEmployeeRepository;
    }

    @Override
    public void publishCertificate(CertificateRequest certificateRequest) {
        CertificateEmployee certificateEmployee = new CertificateEmployee();
        certificateEmployee.setEmployee(certificateRequest.getNominatedTo());
        certificateEmployee.setCertificate(certificateRequest.getCertificate());
        CertificateEmployee saved = certificateEmployeeRepository.save(certificateEmployee);
    }
}
