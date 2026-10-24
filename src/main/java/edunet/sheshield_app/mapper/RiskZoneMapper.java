package edunet.sheshield_app.mapper;

import edunet.sheshield_app.dto.RiskZoneDto;
import edunet.sheshield_app.entity.RiskZone;

/** Converts RiskZone entity -> RiskZoneDto and decides the colour level. */
public class RiskZoneMapper {

    public static RiskZoneDto toDto(RiskZone zone) {
        return new RiskZoneDto(zone.getZoneId(), zone.getAreaName(), zone.getLatitude(),
                zone.getLongitude(), zone.getTimeBand(), zone.getRiskScore(),
                findLevel(zone.getRiskScore()));
    }

    /** 70+ = HIGH, 40-69 = MEDIUM, below 40 = LOW. */
    private static String findLevel(double score) {
        if (score >= 70) return "HIGH";
        if (score >= 40) return "MEDIUM";
        return "LOW";
    }
}
