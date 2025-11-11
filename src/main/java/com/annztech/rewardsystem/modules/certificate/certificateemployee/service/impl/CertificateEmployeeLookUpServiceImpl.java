package com.annztech.rewardsystem.modules.certificate.certificateemployee.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.entity.CertificateEmployee;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.repository.CertificateEmployeeRepository;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.service.CertificateEmployeeLookUpService;
import com.annztech.rewardsystem.modules.certificate.management.entity.Certificate;
import com.annztech.rewardsystem.modules.lookups.service.CertificateLookUpService;
import com.annztech.rewardsystem.modules.metrics.dto.TopEmployeesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CertificateEmployeeLookUpServiceImpl implements CertificateEmployeeLookUpService {

    private final CertificateEmployeeRepository certificateEmployeeRepository;
    private final CertificateLookUpService certificateLookUpService;

    public CertificateEmployeeLookUpServiceImpl(CertificateEmployeeRepository certificateEmployeeRepository, CertificateLookUpService certificateLookUpService) {
        this.certificateEmployeeRepository = certificateEmployeeRepository;
        this.certificateLookUpService = certificateLookUpService;
    }

    @Override
    public List<TopEmployeesDTO> getTopEmployees(Integer month, Integer year) {
        log.info("Fetching top employees in {}/{}...", month, year);
        List<TopEmployeesDTO> topEmployees = certificateEmployeeRepository.findTopEmployees(month, year);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();
        topEmployees.forEach(emp -> {
            if (emp.getProfileURL() != null && !emp.getProfileURL().isEmpty()) {
                emp.setProfileURL(baseUrl + "/" + emp.getProfileURL());
            }
        });
        return topEmployees;
    }

    @Override
    public Long getCertificatesCount(Integer month, Integer year) {
        log.info("Fetching total certificates count in {}/{}...", month, year);
        return certificateEmployeeRepository.getCertificatesCount(month, year);
    }

    @Override
    public List<CertificateEmployee> getAllRewards(String email) {
        log.info("Fetching all rewards for the signed-in employee...");
        return certificateEmployeeRepository.findAllByEmployeeEmail(email);
    }

    public CertificateEmployee getCertificateEmployeeByTemplateIdAndEmployee(UUID templateId, UUID employeeId) {
        List<Certificate> certificates = certificateLookUpService.getCertificateByTemplateId(templateId);
        if (certificates.isEmpty()) {
            throw new AppException("No certificate found for template: " + templateId, HttpStatus.NOT_FOUND);
        }
        return certificateEmployeeRepository.findFirstByEmployeeIdAndCertificateIdIn(employeeId, certificates.stream().map(Certificate::getId).toList())
                .orElseThrow(() -> new AppException("No certificate found for this user and template", HttpStatus.NOT_FOUND));
    }

    @Override
    public Map<String, Long> getTopDepartments(Integer month, Integer year) {
        log.info("Fetching top departments {}/{}...", month, year);
        List<Object[]> results = certificateEmployeeRepository.findTopDepartments(month, year);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).longValue(),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}
