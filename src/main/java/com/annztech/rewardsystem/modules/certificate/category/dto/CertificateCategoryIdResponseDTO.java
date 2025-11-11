package com.annztech.rewardsystem.modules.certificate.category.dto;

import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateCategoryIdResponseDTO {
    public String id;

    public CertificateCategoryIdResponseDTO(CertificateCategory certificateCategory) {
        this.id = certificateCategory.getId().toString();
    }
}
