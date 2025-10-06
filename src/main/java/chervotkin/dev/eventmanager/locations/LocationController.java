package chervotkin.dev.eventmanager.locations;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/locations")
@RestController
public class LocationController {

    private final static Logger log = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;
    private final LocationDtoMapper dtoMapper;

    public LocationController(LocationService locationService, LocationDtoMapper dtoMapper) {
        this.locationService = locationService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getLocations() {
        log.info("Get request for getLocations");
        return ResponseEntity.ok(locationService.getAllLocations()
                .stream()
                .map(dtoMapper::toDto)
                .toList());
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto locationDto
    ) {
        log.info("Get request for create location: locationDto={}", locationDto);
        var createdLocation = locationService.createLocation(
                dtoMapper.toDomain(locationDto)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoMapper.toDto(createdLocation));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteLocation(
            @PathVariable("locationId") Long locationId
    ) {
        log.info("Get request for delete location: locationId={}", locationId);
        var deletedLocation = locationService.deleteLocation(locationId);

        return ResponseEntity.
                status(HttpStatus.NO_CONTENT)
                .body(dtoMapper.toDto(deletedLocation));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocation(
            @PathVariable("locationId") Long locationId
    ) {
        log.info("Get request for get location: locationId={}", locationId);
        var foundLocation = locationService.getLocationById(locationId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoMapper.toDto(foundLocation));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable("locationId") Long locationId,
            @RequestBody @Valid LocationDto updateLocationDto
    ) {
        log.info("Get request for update location:locationId={}, updateLocationDto={}",
                locationId, updateLocationDto);
        var updatedLocation = locationService.updateLocation(
                dtoMapper.toDomain(updateLocationDto),
                locationId
        );

        return ResponseEntity.ok(dtoMapper.toDto(updatedLocation));
    }
}


