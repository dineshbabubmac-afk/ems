package com.annztech.rewardsystem.modules.location.service;

import com.annztech.rewardsystem.modules.location.dto.LocationClientResponseDTO;
import com.annztech.rewardsystem.modules.location.dto.LocationResponseDTO;

public interface LocationStateService {

    LocationResponseDTO deactivate(Long locationId);
    LocationResponseDTO activate(Long locationId);
}
