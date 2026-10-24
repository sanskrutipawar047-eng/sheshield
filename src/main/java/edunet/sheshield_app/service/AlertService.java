package edunet.sheshield_app.service;

import edunet.sheshield_app.dto.AlertDto;
import edunet.sheshield_app.dto.DangerSignalDto;

/** Everything about alerts: create, cancel, send, feedback. */
public interface AlertService {
    AlertDto createAlert(Long userId, DangerSignalDto signals);
    AlertDto getAlert(Long userId, Long alertId);
    void cancelAlert(Long userId, Long alertId, String pin);
    void sendAlert(Long userId, Long alertId);
    void submitFeedback(Long userId, Long alertId, boolean falseAlarm);
}
