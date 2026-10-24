package edunet.sheshield_app.repository;

import edunet.sheshield_app.entity.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {
    List<Evidence> findByUser_UserIdOrderByCapturedAtDesc(Long userId);
}
