package edunet.sheshield_app.dto;

/** Safety report data for the reports page. 'reportedBy' is "Anonymous" or "Registered user". */
public record SafetyReportDto(Long reportId, String areaName, String category,
                              String description, boolean verified, String reportedBy) { }