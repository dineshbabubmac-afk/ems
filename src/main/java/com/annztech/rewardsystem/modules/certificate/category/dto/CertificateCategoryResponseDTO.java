package com.annztech.rewardsystem.modules.certificate.category.dto;

import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateCategoryResponseDTO {
    String id;
    String nameEnglish;
    String nameArabic;
    Boolean isActive;
    String createdAt;
    String updatedAt;

    public CertificateCategoryResponseDTO(CertificateCategory certificateCategory){
        this.id = certificateCategory.getId().toString();
        this.nameEnglish = certificateCategory.getNameEnglish();
        this.nameArabic = certificateCategory.getNameArabic();
        this.isActive = certificateCategory.getIsActive();
        this.createdAt = certificateCategory.getCreatedAt().toString();
        this.updatedAt = certificateCategory.getUpdatedAt().toString();
    }
}
