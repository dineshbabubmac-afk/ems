package com.annztech.rewardsystem.modules.certificate.approval.workflow.service;

import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateApprovalInfoDTO;

import java.util.List;

public interface CertificateRequestApprovalLookupService {
    List<CertificateApprovalInfoDTO> getCertificateApprovalInfo(String certificateRequestId);
}
