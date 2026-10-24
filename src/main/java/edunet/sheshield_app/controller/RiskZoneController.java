package edunet.sheshield_app.controller;

import edunet.sheshield_app.service.RiskZoneService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Page that shows the risk zones table. */
@Controller
public class RiskZoneController {

    private final RiskZoneService riskZoneService;

    public RiskZoneController(RiskZoneService riskZoneService) {
        this.riskZoneService = riskZoneService;
    }

    @GetMapping("/risk-zones")
    public String riskZones(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        model.addAttribute("zones", riskZoneService.getAllZones());
        return "risk-zones";
    }
}
