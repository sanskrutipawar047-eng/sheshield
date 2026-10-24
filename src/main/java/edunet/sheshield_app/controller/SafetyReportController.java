package edunet.sheshield_app.controller;

import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.service.RiskZoneService;
import edunet.sheshield_app.service.SafetyReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Pages for anonymous safety reports. */
@Controller
public class SafetyReportController {

    private final SafetyReportService reportService;
    private final RiskZoneService riskZoneService;

    public SafetyReportController(SafetyReportService reportService, RiskZoneService riskZoneService) {
        this.reportService = reportService;
        this.riskZoneService = riskZoneService;
    }

    /** Report form + list of all reports. */
    @GetMapping("/reports")
    public String reports(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        model.addAttribute("reports", reportService.getAllReports());
        model.addAttribute("zones", riskZoneService.getAllZones());
        return "reports";
    }

    /** If 'anonymous' is ticked, we do not save the user with the report. */
    @PostMapping("/reports/add")
    public String add(@RequestParam Long zoneId, @RequestParam String category,
                      @RequestParam String description,
                      @RequestParam(defaultValue = "false") boolean anonymous,
                      HttpSession session, RedirectAttributes redirect) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        try {
            Long reporterId = anonymous ? null : userId;
            reportService.addReport(reporterId, zoneId, category, description);
            redirect.addFlashAttribute("success", "Thank you. Your report helps other women stay safe.");
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reports";
    }

    /** Only the admin can verify a report. */
    @PostMapping("/reports/verify/{id}")
    public String verify(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        if (!"ADMIN".equals(session.getAttribute("role"))) return "redirect:/login";
        try {
            reportService.verifyReport(id);
            redirect.addFlashAttribute("success", "Report verified.");
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reports";
    }
}
