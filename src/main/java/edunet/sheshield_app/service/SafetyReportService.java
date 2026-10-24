package edunet.sheshield_app.service;

import edunet.sheshield_app.dto.SafetyReportDto;
import java.util.List;

/** What we can do with community safety reports. */
public interface SafetyReportService {
    List<SafetyReportDto> getAllReports();
    SafetyReportDto addReport(Long userId, Long zoneId, String category, String description);
    void verifyReport(Long reportId);
}