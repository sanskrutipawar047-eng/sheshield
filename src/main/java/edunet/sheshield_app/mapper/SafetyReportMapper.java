package edunet.sheshield_app.mapper;

import edunet.sheshield_app.dto.SafetyReportDto;
import edunet.sheshield_app.entity.SafetyReport;

/** Converts SafetyReport entity -> SafetyReportDto. Never reveals who reported. */
public class SafetyReportMapper {
    public static SafetyReportDto toDto(SafetyReport r) {
        String area = r.getRiskZone() == null ? "Unknown area" : r.getRiskZone().getAreaName();
        String by = r.getUser() == null ? "Anonymous" : "Registered user";
        return new SafetyReportDto(r.getReportId(), area, r.getCategory(),
                r.getDescription(), r.isVerified(), by);
    }
}