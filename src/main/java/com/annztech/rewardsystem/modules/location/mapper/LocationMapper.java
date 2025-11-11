package com.annztech.rewardsystem.modules.location.mapper;

import com.annztech.rewardsystem.modules.location.dto.LocationCreateDTO;
import com.annztech.rewardsystem.modules.location.dto.LocationDTO;
import com.annztech.rewardsystem.modules.location.dto.LocationUpdateDTO;
import com.annztech.rewardsystem.modules.location.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    Location toEntity(LocationCreateDTO locationCreateDTO);
    Location toEntity(LocationUpdateDTO locationUpdateDTO);
    LocationDTO toDto(Location location);
}
