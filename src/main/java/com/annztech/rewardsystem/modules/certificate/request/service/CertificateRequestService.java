package com.annztech.rewardsystem.modules.certificate.request.service;

import com.annztech.rewardsystem.modules.certificate.certificateemployee.dto.CertificateEmployeeResponseDTO;
import com.annztech.rewardsystem.modules.certificate.request.dto.*;
import com.annztech.rewardsystem.modules.reports.dto.PagedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CertificateRequestService {
    CertificateRequestIdResponseDTO createCertificateRequest(CertificateRequestCreateDTO dto);
    PagedResponse<CertificateRequestResponseDTO> getAllCertificateRequests(String statusCode, Pageable pageable);
    CertificateRequestIdResponseDTO updateCertificateRequest(String certificateRequestId, CertificateRequestUpdateDTO dto);
    CertificateRequestIdResponseDTO deleteCertificateRequest(String id);
    CertificateRequestResponseDTO getCertificateRequestById(String requestId);
    List<CertificateRequestResponseDTO> search(String search, String actionField, String status);

    CertificateRequestResponseDTO certificatePublish(CertificateRequestPublishDTO certificateRequestPublishDTO);
}
