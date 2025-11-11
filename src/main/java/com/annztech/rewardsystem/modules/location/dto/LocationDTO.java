package com.annztech.rewardsystem.modules.location.dto;

import com.annztech.rewardsystem.modules.location.entity.Location;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private Long id;
    private String nameEn;
    private String nameAr;
    private boolean active;

    public LocationDTO(Location location) {
        this.id = location.getId();
        this.nameEn = location.getNameEn();
        this.nameAr = location.getNameAr();
        this.active = location.getIsActive();
    }
}
