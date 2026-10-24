package edunet.sheshield_app.service;

import edunet.sheshield_app.dto.TrustedContactDto;
import java.util.List;

/** What we can do with trusted contacts. */
public interface TrustedContactService {
    List<TrustedContactDto> getContacts(Long userId);
    TrustedContactDto addContact(Long userId, TrustedContactDto dto);
    void deleteContact(Long userId, Long contactId);
}