package com.annztech.rewardsystem.modules.location.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationResponseDTO {
    long id;
    String nameEn;
    String nameAr;
    private boolean active;

    public LocationResponseDTO(long id, String nameEn, String nameAr, boolean isActive) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        this.active = isActive;
    }

}
