package edunet.sheshield_app.repository;

import edunet.sheshield_app.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUser_UserIdOrderByStartTimeDesc(Long userId);
}
