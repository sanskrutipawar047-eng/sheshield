package edunet.sheshield_app.service.impl;

import edunet.sheshield_app.dto.SafetyReportDto;
import edunet.sheshield_app.entity.RiskZone;
import edunet.sheshield_app.entity.SafetyReport;
import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.mapper.SafetyReportMapper;
import edunet.sheshield_app.repository.RiskZoneRepository;
import edunet.sheshield_app.repository.SafetyReportRepository;
import edunet.sheshield_app.repository.UserRepository;
import edunet.sheshield_app.service.SafetyReportService;
import java.util.List;
import org.springframework.stereotype.Service;

/** Business logic for community safety reports. */
@Service
public class SafetyReportServiceImpl implements SafetyReportService {

    private final SafetyReportRepository reportRepository;
    private final RiskZoneRepository riskZoneRepository;
    private final UserRepository userRepository;

    public SafetyReportServiceImpl(SafetyReportRepository reportRepository,
                                   RiskZoneRepository riskZoneRepository,
                                   UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.riskZoneRepository = riskZoneRepository;
        this.userRepository = userRepository;
    }

    /** Newest reports first. */
    @Override
    public List<SafetyReportDto> getAllReports() {
        return reportRepository.findAllByOrderByReportIdDesc()
                .stream()
                .map(SafetyReportMapper::toDto)
                .toList();
    }

    /** Saves a report. If userId is null, the report is anonymous. */
    @Override
    public SafetyReportDto addReport(Long userId, Long zoneId, String category, String description) {
        if (category == null || category.trim().isEmpty()) {
            throw new SheShieldException("Choose a category.");
        }
        if (description == null || description.trim().length() < 10) {
            throw new SheShieldException("Description must be at least 10 characters.");
        }
        RiskZone zone = riskZoneRepository.findById(zoneId)
                .orElseThrow(() -> new SheShieldException("Area not found."));

        SafetyReport report = new SafetyReport();
        report.setRiskZone(zone);
        report.setCategory(category.trim());
        report.setDescription(description.trim());
        report.setVerified(false);
        if (userId != null) {
            report.setUser(userRepository.findById(userId).orElse(null));
        }
        // TODO Phase 2: verified reports will increase the risk score of the zone
        return SafetyReportMapper.toDto(reportRepository.save(report));
    }

    /** Admin marks a report as verified. */
    @Override
    public void verifyReport(Long reportId) {
        SafetyReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new SheShieldException("Report not found."));
        report.setVerified(true);
        reportRepository.save(report);
    }
}