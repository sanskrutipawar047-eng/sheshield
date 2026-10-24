package edunet.sheshield_app.mapper;

import edunet.sheshield_app.dto.UserDto;
import edunet.sheshield_app.entity.User;

/** Converts User entity <-> UserDto. */
public class UserMapper {

    /** Entity -> DTO. Password and PINs are never sent to the page. */
    public static UserDto toDto(User user) {
        return new UserDto(user.getUserId(), user.getFullName(), user.getPhone(),
                user.getEmail(), null, null, null, user.getRole());
    }

    /** DTO -> Entity. The service adds the hashed password and PINs afterwards. */
    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setFullName(dto.fullName());
        user.setPhone(dto.phone());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        return user;
    }
}