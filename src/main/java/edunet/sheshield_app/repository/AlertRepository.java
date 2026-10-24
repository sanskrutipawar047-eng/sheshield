package edunet.sheshield_app.repository;

import edunet.sheshield_app.entity.Alert;
import edunet.sheshield_app.entity.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUser_UserIdOrderByTriggeredAtDesc(Long userId);
    List<Alert> findTop10ByOrderByTriggeredAtDesc();   // recent alerts for admin
    long countByFalseAlarmTrue();                      // false alarm count for admin
    long countByStatus(AlertStatus status);            // e.g. how many alerts were SENT
}