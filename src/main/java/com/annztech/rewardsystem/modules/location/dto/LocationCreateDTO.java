package com.annztech.rewardsystem.modules.location.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreateDTO {
    @NotBlank(message = "English location name is required")
    private String nameEn;
    @NotBlank(message = "Arabic location name is required")
    private String nameAr;
    private boolean active;
}
