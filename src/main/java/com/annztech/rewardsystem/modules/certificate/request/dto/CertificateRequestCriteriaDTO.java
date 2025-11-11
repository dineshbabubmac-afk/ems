package com.annztech.rewardsystem.modules.certificate.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequestCriteriaDTO {
    private String id;
    private String remarks;
    private MultipartFile file;
}
