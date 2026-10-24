package edunet.sheshield_app.service.impl;

import edunet.sheshield_app.dto.TrustedContactDto;
import edunet.sheshield_app.entity.TrustedContact;
import edunet.sheshield_app.entity.User;
import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.mapper.TrustedContactMapper;
import edunet.sheshield_app.repository.TrustedContactRepository;
import edunet.sheshield_app.repository.UserRepository;
import edunet.sheshield_app.service.TrustedContactService;
import java.util.List;
import org.springframework.stereotype.Service;

/** Business logic for trusted contacts. */
@Service
public class TrustedContactServiceImpl implements TrustedContactService {

    private final TrustedContactRepository contactRepository;
    private final UserRepository userRepository;

    public TrustedContactServiceImpl(TrustedContactRepository contactRepository,
                                     UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TrustedContactDto> getContacts(Long userId) {
        return contactRepository.findByUser_UserId(userId)
                .stream()
                .map(TrustedContactMapper::toDto)
                .toList();
    }

    @Override
    public TrustedContactDto addContact(Long userId, TrustedContactDto dto) {
        if (dto.name() == null || dto.name().trim().isEmpty()) {
            throw new SheShieldException("Contact name is required.");
        }
        if (dto.phone() == null || !dto.phone().trim().matches("\\d{10}")) {
            throw new SheShieldException("Phone number must be 10 digits.");
        }
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new SheShieldException("User not found."));
        TrustedContact saved = contactRepository.save(TrustedContactMapper.toEntity(dto, owner));
        return TrustedContactMapper.toDto(saved);
    }

    /** A user can delete only their own contacts. */
    @Override
    public void deleteContact(Long userId, Long contactId) {
        TrustedContact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new SheShieldException("Contact not found."));
        if (!contact.getUser().getUserId().equals(userId)) {
            throw new SheShieldException("You cannot delete this contact.");
        }
        contactRepository.delete(contact);
    }
}