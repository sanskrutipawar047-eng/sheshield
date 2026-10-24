package edunet.sheshield_app.controller;

import edunet.sheshield_app.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Admin dashboard page. Only the ADMIN role can open it. */
@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String admin(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("role"))) return "redirect:/login";
        model.addAttribute("stats", adminService.getStats());
        model.addAttribute("alerts", adminService.getRecentAlerts());
        return "admin-dashboard";
    }
}
