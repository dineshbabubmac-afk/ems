package com.annztech.rewardsystem.modules.certificate.approval.workflow.service.impl;

import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateApprovalInfoDTO;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.repository.CertificateRequestApprovalRepository;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.service.CertificateRequestApprovalLookupService;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CertificateRequestApprovalLookupServiceImpl extends LocalizationService implements CertificateRequestApprovalLookupService {
    private final CertificateRequestApprovalRepository certificateRequestApprovalRepository;
    public CertificateRequestApprovalLookupServiceImpl(CertificateRequestApprovalRepository certificateRequestApprovalRepository) {
        this.certificateRequestApprovalRepository = certificateRequestApprovalRepository;
    }

    @Override
    public List<CertificateApprovalInfoDTO> getCertificateApprovalInfo(String certificateRequestId) {
        StringsUtils.validateUUID(certificateRequestId, getMessage("certificate.request.approval.invalid.id") + certificateRequestId);
        UUID requestId = UUID.fromString(certificateRequestId);
        boolean hasAdminApp = DomainHelper.hasFromAdminApp();
        if(hasAdminApp){
            return certificateRequestApprovalRepository.findCertificateApprovalDetailsByRequestId(requestId);
        }
        String memberId = DomainHelper.getId();
        if(memberId != null){
            return certificateRequestApprovalRepository.findCertificateApprovalDetailsByRequestIdAndMemberId(requestId, UUID.fromString(memberId));
        }
        return List.of();
    }
}
