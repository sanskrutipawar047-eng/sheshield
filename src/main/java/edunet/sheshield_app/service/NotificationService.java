package edunet.sheshield_app.service;

import edunet.sheshield_app.entity.Alert;

/** Sends alerts to people. Simulated now, real SMS (Twilio) later. */
public interface NotificationService {
    void notifyAll(Alert alert);
}
