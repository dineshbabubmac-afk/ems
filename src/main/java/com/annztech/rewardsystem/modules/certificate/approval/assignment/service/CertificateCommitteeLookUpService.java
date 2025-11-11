package com.annztech.rewardsystem.modules.certificate.approval.assignment.service;

public interface CertificateCommitteeLookUpService {
    Long getCommitteeHeadCount(Integer month, Integer year);
    Long getCommitteeMembersCount(Integer month, Integer year);
}
