package edunet.sheshield_app.repository;

import edunet.sheshield_app.entity.RiskZone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RiskZoneRepository extends JpaRepository<RiskZone, Long> {
    List<RiskZone> findAllByOrderByRiskScoreDesc();
}