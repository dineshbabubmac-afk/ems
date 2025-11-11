package com.annztech.rewardsystem.modules.certificate.approval.workflow.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.constant.CertificateRequestApprovalConstant;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateRequestApprovalResponseDTO;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateRequestApprovalUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.entity.CertificateRequestApproval;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.repository.CertificateRequestApprovalRepository;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.service.CertificateRequestApprovalService;
import com.annztech.rewardsystem.modules.certificate.request.dto.CertificateRequestResponseDTO;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import com.annztech.rewardsystem.modules.lookups.service.CertificateStatusLookUpService;
import com.annztech.rewardsystem.modules.reports.dto.PagedResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CertificateRequestApprovalServiceImpl extends LocalizationService implements CertificateRequestApprovalService {
    private final CertificateRequestApprovalRepository certificateRequestApprovalRepository;
    private final CertificateStatusLookUpService certificateStatusLookUpService;
    public CertificateRequestApprovalServiceImpl(CertificateRequestApprovalRepository certificateRequestApprovalRepository, CertificateStatusLookUpService certificateStatusLookUpService) {
        this.certificateRequestApprovalRepository = certificateRequestApprovalRepository;
        this.certificateStatusLookUpService = certificateStatusLookUpService;
    }

    @Override
    public List<CertificateRequestResponseDTO> rewardApproval(String userId, String status) {
        List<CertificateRequestApproval> certificateRequestApproval = certificateRequestApprovalRepository.findAllByMember_IdAndStatusCode_Code(UUID.fromString(userId), status);
        if(certificateRequestApproval.isEmpty()){
            return List.of();
        } else {
            return certificateRequestApproval.stream().map(CertificateRequestResponseDTO::new).toList();
        }
    }

    @Override
    @Transactional
    public CertificateRequestResponseDTO updateRewardApproval(String id, CertificateRequestApprovalUpdateDTO updateDTO) {
        CertificateRequestApproval certificateRequestApproval = certificateRequestApprovalRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(getMessage(CertificateRequestApprovalConstant.CERTIFICATE_REQUEST_APPROVAL_NOT_FOUND), HttpStatus.NOT_FOUND));
//        if(certificateRequestApproval.getStatusCode().equals("PEN")){
//            throw new AppException("This certificate request approval has already been processed.", HttpStatus.CONFLICT);
//        }
//        if ("REJECTED".equalsIgnoreCase(updateDTO.getStatusCode()) &&
//                (updateDTO.getRejectReason() == null || updateDTO.getRejectReason().isBlank())) {
//            throw new AppException("Reject reason is required when the status is REJECTED", HttpStatus.BAD_REQUEST);
//        }
        certificateRequestApproval.setStatusCode(certificateStatusLookUpService.getCertificateStatusCode(updateDTO.getStatusCode()));
        certificateRequestApproval.setReason(updateDTO.getRejectReason());
        certificateRequestApprovalRepository.save(certificateRequestApproval);
        return new CertificateRequestResponseDTO(certificateRequestApproval);
    }

    @Override
    public PagedResponse<CertificateRequestApprovalResponseDTO> rewardApprovalAll(String status, Boolean isActive, String query, Pageable pageable) {
        if(isActive == null){
            isActive = false;
        }
        Page<CertificateRequestApproval> page = certificateRequestApprovalRepository.findAllFiltered(status, isActive, searchNullCheckQuery(query), pageable);
        List<CertificateRequestApprovalResponseDTO> dtoList = page.getContent().stream().map(CertificateRequestApprovalResponseDTO::new).toList();
        return new PagedResponse<>(
                dtoList,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );    }

    @Override
    public PagedResponse<CertificateRequestApprovalResponseDTO> getAllApproval(String status, Boolean isActive, String query, Pageable pageable) {
        String id = "cbd5d726-95f0-481c-8311-d6d5c939dd8a";
        Page<CertificateRequestApproval> page = certificateRequestApprovalRepository.findAllCertificateRequest(status, isActive, searchNullCheckQuery(query), UUID.fromString(id), pageable);
        List<CertificateRequestApprovalResponseDTO> dtoList = page.getContent().stream().map(CertificateRequestApprovalResponseDTO::new).toList();
        return new PagedResponse<>(
                dtoList,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Override
    public CertificateRequestApprovalResponseDTO getCertificateRequestResponse(String rewardApprovalId) {
        CertificateRequestApproval certificateRequestApproval = certificateRequestApprovalRepository.findById(UUID.fromString(rewardApprovalId)).orElseThrow(() -> new AppException(getMessage("certificate.request.approval.not.found"), HttpStatus.NOT_FOUND));
        return new CertificateRequestApprovalResponseDTO(certificateRequestApproval);
    }


    private String searchNullCheckQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "";
        }
        return query.trim();
    }

}
