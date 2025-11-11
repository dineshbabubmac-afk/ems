package com.annztech.rewardsystem.modules.certificate.certificateemployee.service.impl;

import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.dto.CertificateEmployeeResponseDTO;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.entity.CertificateEmployee;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.repository.CertificateEmployeeRepository;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.service.CertificateEmployeeService;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CertificateEmployeeServiceImpl implements CertificateEmployeeService {
    private final CertificateEmployeeRepository certificateEmployeeRepository;
    public CertificateEmployeeServiceImpl(CertificateEmployeeRepository certificateEmployeeRepository) {
        this.certificateEmployeeRepository = certificateEmployeeRepository;
    }

    @Override
    public List<CertificateEmployeeResponseDTO> getAllMyRewards() {
        String id = DomainHelper.getId();
        List<CertificateEmployee> certificateEmployees = certificateEmployeeRepository.findAllByEmployee_Id(UUID.fromString(id));
        return certificateEmployees.stream().map(CertificateEmployeeResponseDTO::new).toList();
    }
}
