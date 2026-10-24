package edunet.sheshield_app.repository;

import edunet.sheshield_app.entity.AlertResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertResponseRepository extends JpaRepository<AlertResponse, Long> {
    List<AlertResponse> findByAlert_AlertId(Long alertId);
}
