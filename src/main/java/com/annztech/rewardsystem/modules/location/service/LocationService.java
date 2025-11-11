package com.annztech.rewardsystem.modules.location.service;

import com.annztech.rewardsystem.modules.location.dto.LocationCreateDTO;
import com.annztech.rewardsystem.modules.location.dto.LocationDTO;
import com.annztech.rewardsystem.modules.location.dto.LocationUpdateDTO;
import com.annztech.rewardsystem.modules.location.entity.Location;

import java.util.List;

public interface LocationService {
    List<LocationDTO> getAllLocationDTO();
    LocationDTO getLocationDTOById(Long id);
    LocationDTO createLocationFromDTO(LocationCreateDTO locationDTO);
    LocationDTO updateLocationFromDTO(Long id, LocationUpdateDTO locationDTO);
    LocationDTO deleteLocationFromDTO(Long id);

    //For composite service communication
    Location getLocationEntityById(Long id);
    Location getLocationEntityByNameEn(String nameEn);
    List<LocationDTO> searchLocationDTO(String query);
}
