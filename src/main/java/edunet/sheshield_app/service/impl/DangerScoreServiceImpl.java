package edunet.sheshield_app.service.impl;

import edunet.sheshield_app.dto.DangerSignalDto;
import edunet.sheshield_app.entity.AlertLevel;
import edunet.sheshield_app.service.DangerScoreService;
import org.springframework.stereotype.Service;

/** Weighted rule-based danger score (0-100). */
@Service
public class DangerScoreServiceImpl implements DangerScoreService {

    // Weight of each signal. Total = 100.
    private static final int SCREAM = 30;
    private static final int JERK = 25;
    private static final int HEART_RATE = 20;
    private static final int ISOLATED = 15;
    private static final int LATE_NIGHT = 10;

    /** Adds the weight of every signal that is ON. */
    @Override
    public double calculateScore(DangerSignalDto s) {
        double score = 0;
        if (s.scream()) score += SCREAM;
        if (s.jerk()) score += JERK;
        if (s.heartRate()) score += HEART_RATE;
        if (s.isolated()) score += ISOLATED;
        if (s.lateNight()) score += LATE_NIGHT;
        // TODO Phase 2: replace this with the CNN / XGBoost model output
        return score;
    }

    /** Below 40 = LOW, 40 to 69 = MEDIUM, 70 and above = HIGH. */
    @Override
    public AlertLevel findLevel(double score) {
        if (score >= 70) return AlertLevel.HIGH;
        if (score >= 40) return AlertLevel.MEDIUM;
        return AlertLevel.LOW;
    }
}
