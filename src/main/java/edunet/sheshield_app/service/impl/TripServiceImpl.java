package edunet.sheshield_app.service.impl;

import edunet.sheshield_app.dto.TripDto;
import edunet.sheshield_app.entity.Trip;
import edunet.sheshield_app.entity.TripStatus;
import edunet.sheshield_app.entity.User;
import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.mapper.TripMapper;
import edunet.sheshield_app.repository.TripRepository;
import edunet.sheshield_app.repository.UserRepository;
import edunet.sheshield_app.service.TripService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/** Business logic for trip sharing. */
@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripServiceImpl(TripRepository tripRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    /** All trips of the user, newest first. */
    @Override
    public List<TripDto> getTrips(Long userId) {
        return tripRepository.findByUser_UserIdOrderByStartTimeDesc(userId)
                .stream()
                .map(TripMapper::toDto)
                .toList();
    }

    /** Starts a new trip. Only one ACTIVE trip is allowed at a time. */
    @Override
    public TripDto startTrip(Long userId, String destination) {
        if (destination == null || destination.trim().isEmpty()) {
            throw new SheShieldException("Enter a destination.");
        }
        boolean hasActive = tripRepository.findByUser_UserIdOrderByStartTimeDesc(userId)
                .stream()
                .anyMatch(t -> t.getStatus() == TripStatus.ACTIVE);
        if (hasActive) {
            throw new SheShieldException("You already have an active trip. End it first.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SheShieldException("User not found."));

        Trip trip = new Trip();
        trip.setUser(user);
        trip.setDestination(destination.trim());
        trip.setStartTime(LocalDateTime.now());
        trip.setStatus(TripStatus.ACTIVE);
        // TODO Phase 2: share live GPS with trusted contacts and detect off-route / long stops
        return TripMapper.toDto(tripRepository.save(trip));
    }

    /** Marks the trip as COMPLETED. A user can end only their own trip. */
    @Override
    public void endTrip(Long userId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new SheShieldException("Trip not found."));
        if (!trip.getUser().getUserId().equals(userId)) {
            throw new SheShieldException("This is not your trip.");
        }
        trip.setStatus(TripStatus.COMPLETED);
        tripRepository.save(trip);
    }
}