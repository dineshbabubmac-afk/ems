package com.annztech.rewardsystem.modules.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateDTO {
    private String nameEn;
    private String nameAr;
    private boolean active;
}
