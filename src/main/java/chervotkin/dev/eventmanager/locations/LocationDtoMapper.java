package chervotkin.dev.eventmanager.locations;

import org.springframework.stereotype.Component;

@Component
public class LocationDtoMapper {

    public LocationDto toDto(Location location) {
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public Location toDomain(LocationDto location) {
        return new Location(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

}
