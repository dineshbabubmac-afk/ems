package com.annztech.rewardsystem.modules.location.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.openApi.StandardApiResponses;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.location.constants.LocationConstant;
import com.annztech.rewardsystem.modules.location.dto.LocationCreateDTO;
import com.annztech.rewardsystem.modules.location.dto.LocationDTO;
import com.annztech.rewardsystem.modules.location.dto.LocationUpdateDTO;
import com.annztech.rewardsystem.modules.location.service.LocationService;
import com.annztech.rewardsystem.modules.location.service.LocationStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/location")
@RestController
@Tag(name = "Location", description = "Location Management")
public class LocationController extends LocalizationService {

    private final LocationService locationService;
    private final LocationStateService locationStateService;

    LocationController(LocationService locationService, LocationStateService locationStateService) {
        this.locationService = locationService;
        this.locationStateService = locationStateService;
    }

    @GetMapping
    @Operation(summary = "Get all location")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns all locations",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LocationDTO.class))
                    )
            ),
    })
    public ResponseEntity<Object> getLocations() {
        return AppResponse.success(getMessage(LocationConstant.LOCATION_FETCHED), HttpStatus.OK, locationService.getAllLocationDTO());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get location by ID",
            description = "Returns a location by its unique ID"
    )
    @Parameter(description = "Location ID", example = "1")
    @StandardApiResponses
    public ResponseEntity<Object> getLocation(@PathVariable Long id) {
        return AppResponse.success(getMessage(LocationConstant.LOCATION_FETCHED), HttpStatus.OK, locationService.getLocationDTOById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new location")
    @StandardApiResponses
    public ResponseEntity<Object> addLocation(@Valid @RequestBody LocationCreateDTO location) {
        return AppResponse.success(getMessage(LocationConstant.LOCATION_CREATED), HttpStatus.CREATED, locationService.createLocationFromDTO(location));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update location")
    @StandardApiResponses
    public ResponseEntity<Object> updateLocation(@PathVariable Long id, @RequestBody LocationUpdateDTO location) {
        return AppResponse.success(getMessage(LocationConstant.LOCATION_UPDATED), HttpStatus.CREATED, locationService.updateLocationFromDTO(id, location));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete location")
    @StandardApiResponses
    public ResponseEntity<Object> deleteLocation(@PathVariable Long id) {
        return AppResponse.success(getMessage(LocationConstant.LOCATION_DELETED), HttpStatus.OK, locationService.deleteLocationFromDTO(id));
    }

    @GetMapping("/search")
    @Operation(summary = "search by location names")
    @StandardApiResponses
    public ResponseEntity<Object> searchLocation(@RequestParam("q") String query) {
        return AppResponse.success(getMessage(LocationConstant.LOCATION_FETCHED), HttpStatus.OK, locationService.searchLocationDTO(query));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "deactivate by location name")
    @StandardApiResponses
    public ResponseEntity<Object> deactivateLocation(@PathVariable Long id) {
        return AppResponse.success(getMessage(LocationConstant.LOCATION_DEACTIVATED), HttpStatus.OK, locationStateService.deactivate(id));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "activate by location name")
    @StandardApiResponses
    public ResponseEntity<Object> activateLocation(@PathVariable Long id) {
        return AppResponse.success(getMessage(LocationConstant.LOCATION_ACTIVATED), HttpStatus.OK, locationStateService.activate(id));
    }
}
