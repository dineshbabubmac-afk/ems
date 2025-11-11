package com.annztech.rewardsystem.modules.certificate.management.dto;

import com.annztech.rewardsystem.modules.certificate.management.entity.Certificate;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CertificateIdResponseDTO {
    private String id;

    public CertificateIdResponseDTO(Certificate certificate) {
        this.id = certificate.getId().toString();
    }
}
