package edunet.sheshield_app.dto;

import edunet.sheshield_app.entity.TripStatus;
import java.time.LocalDateTime;

/** Trip data shown on the trips page. */
public record TripDto(Long tripId, String destination, LocalDateTime startTime, TripStatus status) { }