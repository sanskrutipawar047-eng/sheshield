package edunet.sheshield_app.mapper;

import edunet.sheshield_app.dto.AlertDto;
import edunet.sheshield_app.entity.Alert;
import java.util.List;

/** Converts Alert entity -> AlertDto. */
public class AlertMapper {

    public static AlertDto toDto(Alert alert, List<String> notifications) {
        return new AlertDto(alert.getAlertId(), alert.getUser().getFullName(),
                alert.getDangerScore(), alert.getAlertLevel(), alert.getStatus(),
                alert.getTriggeredAt(), alert.getFalseAlarm(), notifications);
    }
}

