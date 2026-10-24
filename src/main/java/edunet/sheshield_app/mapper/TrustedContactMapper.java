package edunet.sheshield_app.mapper;

import edunet.sheshield_app.dto.TrustedContactDto;
import edunet.sheshield_app.entity.TrustedContact;
import edunet.sheshield_app.entity.User;

/**
 * Converts TrustedContact entity to TrustedContactDto
 * and TrustedContactDto to TrustedContact entity.
 */
public class TrustedContactMapper {

    private TrustedContactMapper() {
        // Prevent object creation; this class contains only static methods.
    }

    public static TrustedContactDto toDto(TrustedContact contact) {

        if (contact == null) {
            return null;
        }

        return new TrustedContactDto(
                contact.getContactId(),
                contact.getName(),
                contact.getPhone()
        );
    }

    public static TrustedContact toEntity(TrustedContactDto dto, User owner) {

        if (dto == null) {
            return null;
        }

        TrustedContact contact = new TrustedContact();

        contact.setName(dto.name());
        contact.setPhone(dto.phone());
        contact.setUser(owner);

        return contact;
    }
}

