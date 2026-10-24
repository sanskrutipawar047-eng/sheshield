package edunet.sheshield_app.service.impl;

import edunet.sheshield_app.dto.LoginDto;
import edunet.sheshield_app.dto.UserDto;
import edunet.sheshield_app.entity.Role;
import edunet.sheshield_app.entity.User;
import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.mapper.UserMapper;
import edunet.sheshield_app.repository.UserRepository;
import edunet.sheshield_app.service.UserService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register a new user
    @Override
    public UserDto registerUser(UserDto dto) {

        if (dto == null
                || isBlank(dto.fullName())
                || isBlank(dto.email())
                || isBlank(dto.password())) {

            throw new SheShieldException(
                    "Name, email and password are required.");
        }

        if (dto.password().length() < 6) {
            throw new SheShieldException(
                    "Password must be at least 6 characters.");
        }

        // Validate both PINs: exactly four digits
        if (dto.cancelPin() == null
                || !dto.cancelPin().matches("\\d{4}")
                || dto.duressPin() == null
                || !dto.duressPin().matches("\\d{4}")) {

            throw new SheShieldException(
                    "Both PINs must be exactly 4 digits.");
        }

        // Cancel PIN and Duress PIN must be different
        if (dto.cancelPin().equals(dto.duressPin())) {
            throw new SheShieldException(
                    "Cancel PIN and Duress PIN must be different.");
        }

        String email = dto.email().trim().toLowerCase();

        // Check whether the email is already registered
        if (userRepository.existsByEmail(email)) {
            throw new SheShieldException(
                    "This email is already registered.");
        }

        // Convert DTO into entity
        User user = UserMapper.toEntity(dto);

        user.setEmail(email);

        // Hash password and PINs before saving
        user.setPasswordHash(encoder.encode(dto.password()));
        user.setCancelPinHash(encoder.encode(dto.cancelPin()));
        user.setDuressPinHash(encoder.encode(dto.duressPin()));

        // Assign the default role
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        // Save user to database
        User savedUser = userRepository.save(user);

        return UserMapper.toDto(savedUser);
    }

    // Login an existing user
    @Override
    public UserDto login(LoginDto dto) {

        if (dto == null
                || isBlank(dto.email())
                || isBlank(dto.password())) {

            throw new SheShieldException(
                    "Enter email and password.");
        }

        String email = dto.email().trim().toLowerCase();

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new SheShieldException(
                                "Invalid email or password."));

        // Verify password
        if (!encoder.matches(
                dto.password(), user.getPasswordHash())) {

            throw new SheShieldException(
                    "Invalid email or password.");
        }

        return UserMapper.toDto(user);
    }

    // Get user by ID
    @Override
    public UserDto getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new SheShieldException("User not found."));

        return UserMapper.toDto(user);
    }

    // Check whether a string is empty
    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}