package com.annztech.rewardsystem.modules.certificate.management.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateUpdateDTO {
    private String categoryId;
    private String templateId;
    private String name;
    private String description;
    private List<CertificateCreateDTO.CriterionCreateDTO> criteriaList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class CriterionCreateDTO {

        private String name;
        @Min(0)
        @Max(100)
        private Integer percentage;
        private String description;
    }
}
