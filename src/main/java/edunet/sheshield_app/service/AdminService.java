package edunet.sheshield_app.service;

import edunet.sheshield_app.dto.AdminStatsDto;
import edunet.sheshield_app.dto.AlertDto;
import java.util.List;

/** Data for the admin dashboard. */
public interface AdminService {
    AdminStatsDto getStats();
    List<AlertDto> getRecentAlerts();
}