package edunet.sheshield_app.dto;

import edunet.sheshield_app.entity.AlertLevel;
import edunet.sheshield_app.entity.AlertStatus;
import java.time.LocalDateTime;
import java.util.List;

/** Alert data shown on pages, including who was notified. */
public record AlertDto(Long alertId, String userName, double dangerScore,
                       AlertLevel alertLevel, AlertStatus status,
                       LocalDateTime triggeredAt, Boolean falseAlarm,
                       List<String> notifications) { }
