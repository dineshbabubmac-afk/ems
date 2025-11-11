package com.annztech.rewardsystem.modules.certificate.request.dto;

import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateRequestIdResponseDTO {
    String certificateCode;

    public CertificateRequestIdResponseDTO(CertificateRequest certificateCode) {
        this.certificateCode = certificateCode.getId().toString();
    }
}
