package edunet.sheshield_app.repository;

import edunet.sheshield_app.entity.SafetyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SafetyReportRepository extends JpaRepository<SafetyReport, Long> {
    List<SafetyReport> findAllByOrderByReportIdDesc();
}