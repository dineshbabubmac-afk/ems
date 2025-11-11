package com.annztech.rewardsystem.modules.location.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.ValidationUtils;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import com.annztech.rewardsystem.modules.location.constants.LocationConstant;
import com.annztech.rewardsystem.modules.location.dto.LocationCreateDTO;
import com.annztech.rewardsystem.modules.location.dto.LocationDTO;
import com.annztech.rewardsystem.modules.location.dto.LocationUpdateDTO;
import com.annztech.rewardsystem.modules.location.entity.Location;
import com.annztech.rewardsystem.modules.location.mapper.LocationMapper;
import com.annztech.rewardsystem.modules.location.repository.LocationRepository;
import com.annztech.rewardsystem.modules.location.service.LocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl extends LocalizationService implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public List<LocationDTO> getAllLocationDTO() {
        List<Location> locationList = locationRepository.findAllLocations(DomainHelper.sortByUpdatedAtDesc());
        return locationList.stream().map(LocationDTO::new).toList();
    }

    @Override
    public LocationDTO getLocationDTOById(Long id) {
        Location locationEntity = validateAndGetLocation(id);
        return locationMapper.toDto(locationEntity);
    }

    @Override
    public LocationDTO createLocationFromDTO(LocationCreateDTO locationDTO) {
        Location locationEntity = locationMapper.toEntity(locationDTO);
        Location savedLocation = locationRepository.save(locationEntity);
        return locationMapper.toDto(savedLocation);
    }

    @Override
    public LocationDTO updateLocationFromDTO(Long id, LocationUpdateDTO locationDTO) {
        Location locationEntity = validateAndGetLocation(id);
        if (StringUtils.isNotBlank(locationDTO.getNameEn())) {
            locationEntity.setNameEn(locationDTO.getNameEn());
        }
        if (StringUtils.isNotBlank(locationDTO.getNameAr())) {
            locationEntity.setNameAr(locationDTO.getNameAr());
        }
        return locationMapper.toDto(locationRepository.save(locationEntity));
    }

    @Override
    public LocationDTO deleteLocationFromDTO(Long id) {
        Location locationEntity = validateAndGetLocation(id);
        locationEntity.setIsActive(false);
        locationEntity.setIsDeleted(true);
        locationRepository.save(locationEntity);
        return locationMapper.toDto(locationEntity);
    }

    @Override
    public List<LocationDTO> searchLocationDTO(String query) {
        if (StringUtils.isBlank(query)) {
            throw new AppException(getMessage(LocationConstant.LOCATION_ID_REQUIRED), HttpStatus.BAD_REQUEST);
        }
        List<Location> locations = locationRepository.searchByKeyword(query);
        return locations.stream().map(LocationDTO::new).toList();
    }

    @Override
    public Location getLocationEntityById(Long id) {
        return validateAndGetLocation(id);
    }

    @Override
    public Location getLocationEntityByNameEn(String nameEn) {
        if(StringUtils.isBlank(nameEn)){
            throw new AppException(getMessage(LocationConstant.LOCATION_NAME_REQUIRED), HttpStatus.BAD_REQUEST);
        }
        return locationRepository.findLocationByNameEn(nameEn).orElseThrow(() -> new AppException(getMessage(LocationConstant.LOCATION_NO_RECORDS), HttpStatus.NOT_FOUND));
    }

    private Location validateAndGetLocation(Long id) {
        if (!ValidationUtils.isValidId(id)) {
            throw new AppException(getMessage(LocationConstant.LOCATION_ID_REQUIRED), HttpStatus.BAD_REQUEST);
        }
        return locationRepository.findById(id).orElseThrow(() -> new AppException(getMessage(LocationConstant.LOCATION_NO_RECORDS), HttpStatus.NOT_FOUND));
    }
}
