package edunet.sheshield_app.service.impl;

import edunet.sheshield_app.entity.Alert;
import edunet.sheshield_app.entity.AlertResponse;
import edunet.sheshield_app.entity.ResponderType;
import edunet.sheshield_app.entity.TrustedContact;
import edunet.sheshield_app.repository.AlertResponseRepository;
import edunet.sheshield_app.repository.TrustedContactRepository;
import edunet.sheshield_app.service.NotificationService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/** Simulated notifications. Each one is saved as an AlertResponse row. */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final AlertResponseRepository responseRepository;
    private final TrustedContactRepository contactRepository;

    public NotificationServiceImpl(AlertResponseRepository responseRepository,
                                   TrustedContactRepository contactRepository) {
        this.responseRepository = responseRepository;
        this.contactRepository = contactRepository;
    }

    /** Notifies trusted contacts, guardian and police. */
    @Override
    public void notifyAll(Alert alert) {
        for (TrustedContact c : contactRepository.findByUser_UserId(alert.getUser().getUserId())) {
            // TODO Phase 2: send a real SMS with Twilio here
            save(alert, ResponderType.CONTACT, "SMS sent to " + c.getName() + " (" + c.getPhone() + ")");
        }
        save(alert, ResponderType.GUARDIAN, "Nearest guardian volunteer notified with live location");
        save(alert, ResponderType.POLICE, "Alert forwarded to police helpline 112");
    }

    private void save(Alert alert, ResponderType type, String action) {
        AlertResponse response = new AlertResponse();
        response.setAlert(alert);
        response.setResponderType(type);
        response.setAction(action);
        response.setRespondedAt(LocalDateTime.now());
        responseRepository.save(response);
    }
}

