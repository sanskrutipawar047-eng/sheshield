package edunet.sheshield_app.dto;

/** Risk zone data for the table. 'level' is LOW, MEDIUM or HIGH. */
public record RiskZoneDto(Long zoneId, String areaName, double latitude, double longitude,
                          String timeBand, double riskScore, String level) { }
