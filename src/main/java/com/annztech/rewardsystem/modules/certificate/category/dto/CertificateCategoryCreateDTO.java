package com.annztech.rewardsystem.modules.certificate.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateCategoryCreateDTO {
    @NotNull(message = "Certificate category name is required")
    private String nameEnglish;
    @NotNull(message = "Certificate category name in Arabic is required")
    private String nameArabic;
}
