package com.annztech.rewardsystem.modules.certificate.certificateemployee.service;

import com.annztech.rewardsystem.modules.certificate.certificateemployee.entity.CertificateEmployee;
import com.annztech.rewardsystem.modules.metrics.dto.TopEmployeesDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CertificateEmployeeLookUpService {
    Map<String, Long> getTopDepartments(Integer month, Integer year);
    List<TopEmployeesDTO> getTopEmployees(Integer month, Integer year);
    Long getCertificatesCount(Integer month, Integer year);
    List<CertificateEmployee> getAllRewards(String email);
    CertificateEmployee getCertificateEmployeeByTemplateIdAndEmployee(UUID templateId, UUID id);
}
