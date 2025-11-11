package com.annztech.rewardsystem.modules.certificate.management.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateCreateDTO {
    @NotBlank(message = "Category ID is required")
    private String categoryId;
    @NotBlank(message = "Template ID is required")
    private String templateId;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Criteria field is required")
    private List<CriterionCreateDTO> criteriaList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CriterionCreateDTO {

        @NotBlank
        private String name;
        @NotNull
        @Min(0)
        @Max(100)
        private Integer percentage;
        private String description;
    }
}
