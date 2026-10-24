package edunet.sheshield_app.service;

import edunet.sheshield_app.dto.DangerSignalDto;
import edunet.sheshield_app.entity.AlertLevel;

/** Calculates how dangerous a situation is. */
public interface DangerScoreService {
    double calculateScore(DangerSignalDto signals);
    AlertLevel findLevel(double score);
}
