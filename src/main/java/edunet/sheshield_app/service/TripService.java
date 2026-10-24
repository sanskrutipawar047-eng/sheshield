package edunet.sheshield_app.service;

import edunet.sheshield_app.dto.TripDto;
import java.util.List;

/** What we can do with trips. */
public interface TripService {
    List<TripDto> getTrips(Long userId);
    TripDto startTrip(Long userId, String destination);
    void endTrip(Long userId, Long tripId);
}