package com.annztech.rewardsystem.modules.certificate.approval.workflow.service;

import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateApprovalInfoDTO;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateRequestApprovalResponseDTO;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateRequestApprovalUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.request.dto.CertificateRequestResponseDTO;
import com.annztech.rewardsystem.modules.reports.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CertificateRequestApprovalService {
    List<CertificateRequestResponseDTO> rewardApproval(String userId, String status);
    CertificateRequestResponseDTO updateRewardApproval(String id, CertificateRequestApprovalUpdateDTO updateDTO);
    PagedResponse<CertificateRequestApprovalResponseDTO> rewardApprovalAll(String status, Boolean isActive, String query, Pageable pageable);
    PagedResponse<CertificateRequestApprovalResponseDTO> getAllApproval(String status, Boolean isActive, String query, Pageable pageable);
    CertificateRequestApprovalResponseDTO getCertificateRequestResponse(String rewardApprovalId);
}
