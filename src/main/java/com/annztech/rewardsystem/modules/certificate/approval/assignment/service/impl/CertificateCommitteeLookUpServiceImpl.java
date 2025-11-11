package com.annztech.rewardsystem.modules.certificate.approval.assignment.service.impl;

import com.annztech.rewardsystem.modules.certificate.approval.assignment.repository.CertificateCommitteeRepository;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.service.CertificateCommitteeLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CertificateCommitteeLookUpServiceImpl implements CertificateCommitteeLookUpService {

    private final CertificateCommitteeRepository certificateCommitteeRepository;

    public CertificateCommitteeLookUpServiceImpl(CertificateCommitteeRepository certificateCommitteeRepository) {
        this.certificateCommitteeRepository = certificateCommitteeRepository;
    }

    @Override
    public Long getCommitteeHeadCount(Integer month, Integer year) {
        return certificateCommitteeRepository.countCommitteeHeadsByMonthYear(month, year);
    }

    @Override
    public Long getCommitteeMembersCount(Integer month, Integer year) {
        return certificateCommitteeRepository.countCommitteeMembersByMonthYear(month, year);
    }
}
