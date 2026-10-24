package edunet.sheshield_app.dto;

/** Numbers shown on the admin dashboard. */
public record AdminStatsDto(long totalUsers, long totalAlerts, long sentAlerts, long falseAlarms) { }