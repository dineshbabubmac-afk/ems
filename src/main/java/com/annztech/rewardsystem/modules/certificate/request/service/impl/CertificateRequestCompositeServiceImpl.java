package com.annztech.rewardsystem.modules.certificate.request.service.impl;

import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.service.CertificateCommitteeService;
import com.annztech.rewardsystem.modules.certificate.request.service.CertificateRequestCompositeService;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateRequestCompositeServiceImpl extends LocalizationService implements CertificateRequestCompositeService {
    private final CertificateCommitteeService certificateCommitteeService;

    public CertificateRequestCompositeServiceImpl(CertificateCommitteeService certificateCommitteeService) {
        this.certificateCommitteeService = certificateCommitteeService;
    }

    @Override
    public List<Employee> getAllParticularCommitteeMembers() {
        return certificateCommitteeService.getAllCommitteeMembers();
    }
}
