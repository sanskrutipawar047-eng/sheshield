package edunet.sheshield_app.service;

import edunet.sheshield_app.dto.LoginDto;
import edunet.sheshield_app.dto.UserDto;

/** What we can do with users. */
public interface UserService {
    UserDto registerUser(UserDto userDto);
    UserDto login(LoginDto loginDto);
    UserDto getUserById(Long userId);
}