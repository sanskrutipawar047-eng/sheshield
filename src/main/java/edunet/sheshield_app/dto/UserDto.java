package edunet.sheshield_app.dto;

import edunet.sheshield_app.entity.Role;

/** User data moving between pages and services. Password/PINs are only used while registering. */
public record UserDto(Long userId, String fullName, String phone, String email,
        String password, String cancelPin, String duressPin, Role role) { }
