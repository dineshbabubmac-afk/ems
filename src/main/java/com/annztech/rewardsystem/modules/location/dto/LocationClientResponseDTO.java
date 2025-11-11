package com.annztech.rewardsystem.modules.location.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationClientResponseDTO {
    long id;
    String clientId;
    Boolean isActive;

    public LocationClientResponseDTO(long id, String clientId, Boolean isActive) {
        this.id = id;
        this.clientId = clientId;
        this.isActive = isActive;
    }

}
