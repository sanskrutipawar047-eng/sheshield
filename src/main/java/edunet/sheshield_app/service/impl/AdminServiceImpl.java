package edunet.sheshield_app.service.impl;

import edunet.sheshield_app.dto.AdminStatsDto;
import edunet.sheshield_app.dto.AlertDto;
import edunet.sheshield_app.entity.AlertStatus;
import edunet.sheshield_app.mapper.AlertMapper;
import edunet.sheshield_app.repository.AlertRepository;
import edunet.sheshield_app.repository.UserRepository;
import edunet.sheshield_app.service.AdminService;
import java.util.List;
import org.springframework.stereotype.Service;

/** Business logic for the admin dashboard. */
@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final AlertRepository alertRepository;

    public AdminServiceImpl(UserRepository userRepository, AlertRepository alertRepository) {
        this.userRepository = userRepository;
        this.alertRepository = alertRepository;
    }

    /** Counts users, alerts, sent alerts and false alarms. */
    @Override
    public AdminStatsDto getStats() {
        return new AdminStatsDto(
                userRepository.count(),
                alertRepository.count(),
                alertRepository.countByStatus(AlertStatus.SENT),
                alertRepository.countByFalseAlarmTrue());
    }

    /** Last 10 alerts, newest first. */
    @Override
    public List<AlertDto> getRecentAlerts() {
        return alertRepository.findTop10ByOrderByTriggeredAtDesc()
                .stream()
                .map(a -> AlertMapper.toDto(a, List.of()))
                .toList();
    }
}
