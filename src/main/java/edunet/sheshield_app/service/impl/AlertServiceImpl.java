package edunet.sheshield_app.service.impl;

import edunet.sheshield_app.dto.AlertDto;
import edunet.sheshield_app.dto.DangerSignalDto;
import edunet.sheshield_app.entity.Alert;
import edunet.sheshield_app.entity.AlertLevel;
import edunet.sheshield_app.entity.AlertStatus;
import edunet.sheshield_app.entity.User;
import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.mapper.AlertMapper;
import edunet.sheshield_app.repository.AlertRepository;
import edunet.sheshield_app.repository.AlertResponseRepository;
import edunet.sheshield_app.repository.UserRepository;
import edunet.sheshield_app.service.AlertService;
import edunet.sheshield_app.service.DangerScoreService;
import edunet.sheshield_app.service.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/** Business logic for alerts: cancel window, duress PIN, sending SOS, feedback. */
@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
    private final AlertResponseRepository responseRepository;
    private final DangerScoreService dangerScoreService;
    private final NotificationService notificationService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AlertServiceImpl(AlertRepository alertRepository, UserRepository userRepository,
                            AlertResponseRepository responseRepository,
                            DangerScoreService dangerScoreService,
                            NotificationService notificationService) {
        this.alertRepository = alertRepository;
        this.userRepository = userRepository;
        this.responseRepository = responseRepository;
        this.dangerScoreService = dangerScoreService;
        this.notificationService = notificationService;
    }

    /** Calculates the score and saves a new alert. LOW is only logged, others wait (PENDING). */
    @Override
    public AlertDto createAlert(Long userId, DangerSignalDto signals) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SheShieldException("User not found."));
        double score = dangerScoreService.calculateScore(signals);
        AlertLevel level = dangerScoreService.findLevel(score);

        Alert alert = new Alert();
        alert.setUser(user);
        alert.setDangerScore(score);
        alert.setAlertLevel(level);
        alert.setTriggeredAt(LocalDateTime.now());
        // LOW = log only, no action needed. MEDIUM/HIGH wait for the user.
        alert.setStatus(level == AlertLevel.LOW ? AlertStatus.CANCELLED : AlertStatus.PENDING);
        return toDto(alertRepository.save(alert));
    }

    @Override
    public AlertDto getAlert(Long userId, Long alertId) {
        return toDto(findOwnedAlert(userId, alertId));
    }

    /**
     * Cancel window.
     * Normal PIN  -> alert is really cancelled.
     * Duress PIN  -> user sees "cancelled" but a silent SOS is sent.
     */
    @Override
    public void cancelAlert(Long userId, Long alertId, String pin) {
        Alert alert = findOwnedAlert(userId, alertId);
        if (alert.getStatus() != AlertStatus.PENDING) {
            throw new SheShieldException("This alert is already closed.");
        }
        if (pin == null || pin.isBlank()) {
            throw new SheShieldException("Enter your PIN.");
        }
        User user = alert.getUser();
        if (encoder.matches(pin, user.getCancelPinHash())) {
            alert.setStatus(AlertStatus.CANCELLED);
            alertRepository.save(alert);
        } else if (encoder.matches(pin, user.getDuressPinHash())) {
            markSent(alert);                       // silent SOS
        } else {
            throw new SheShieldException("Wrong PIN.");
        }
    }

    /** Sends the SOS (called by the countdown or the Send SOS button). */
    @Override
    public void sendAlert(Long userId, Long alertId) {
        Alert alert = findOwnedAlert(userId, alertId);
        if (alert.getStatus() == AlertStatus.PENDING) {
            markSent(alert);
        }
    }

    /** Saves "Was this a real emergency?" answer. This is the training label for the ML model later. */
    @Override
    public void submitFeedback(Long userId, Long alertId, boolean falseAlarm) {
        Alert alert = findOwnedAlert(userId, alertId);
        alert.setFalseAlarm(falseAlarm);
        alertRepository.save(alert);
    }

    // ---------- small helper methods ----------

    private void markSent(Alert alert) {
        alert.setStatus(AlertStatus.SENT);
        alertRepository.save(alert);
        notificationService.notifyAll(alert);
    }

    /** Finds an alert and makes sure it belongs to this user. */
    private Alert findOwnedAlert(Long userId, Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new SheShieldException("Alert not found."));
        if (!alert.getUser().getUserId().equals(userId)) {
            throw new SheShieldException("This is not your alert.");
        }
        return alert;
    }

    private AlertDto toDto(Alert alert) {
        List<String> notifications = responseRepository.findByAlert_AlertId(alert.getAlertId())
                .stream()
                .map(r -> r.getResponderType() + ": " + r.getAction())
                .toList();
        return AlertMapper.toDto(alert, notifications);
    }
}