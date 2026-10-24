package edunet.sheshield_app.mapper;

import edunet.sheshield_app.dto.TripDto;
import edunet.sheshield_app.entity.Trip;

/** Converts Trip entity -> TripDto. */
public class TripMapper {
    public static TripDto toDto(Trip trip) {
        return new TripDto(trip.getTripId(), trip.getDestination(),
                trip.getStartTime(), trip.getStatus());
    }
}