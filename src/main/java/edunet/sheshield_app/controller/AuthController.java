package edunet.sheshield_app.controller;

import edunet.sheshield_app.dto.LoginDto;
import edunet.sheshield_app.dto.UserDto;
import edunet.sheshield_app.entity.Role;
import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Pages for register, login and logout. */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginDto loginDto, HttpSession session, RedirectAttributes redirect) {
        try {
            UserDto user = userService.login(loginDto);
            session.setAttribute("userId", user.userId());
            session.setAttribute("userName", user.fullName());
            session.setAttribute("role", user.role().name());
            return user.role() == Role.ADMIN ? "redirect:/admin" : "redirect:/dashboard";
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(UserDto form, RedirectAttributes redirect) {
        try {
            // Role is set here (not taken from the form) so nobody can register as ADMIN
            UserDto safeDto = new UserDto(null, form.fullName(), form.phone(), form.email(),
                    form.password(), form.cancelPin(), form.duressPin(), Role.USER);
            userService.registerUser(safeDto);
            redirect.addFlashAttribute("success", "Registered successfully. Please login.");
            return "redirect:/login";
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}