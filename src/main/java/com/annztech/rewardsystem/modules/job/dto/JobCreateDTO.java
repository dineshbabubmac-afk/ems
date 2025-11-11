package com.annztech.rewardsystem.modules.job.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobCreateDTO {
    @NotBlank(message = "English job title is required")
    private String titleEn;
    @NotBlank(message = "Arabic job title is required")
    private String titleAr;
    @NotBlank(message = "band level is required")
    private String bandLevel;
}
