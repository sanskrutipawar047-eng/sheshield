package edunet.sheshield_app.controller;

import edunet.sheshield_app.dto.AlertDto;
import edunet.sheshield_app.dto.DangerSignalDto;
import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.service.AlertService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Pages for the SOS flow: simulate danger, cancel, send, feedback. */
@Controller
public class SosController {

    private final AlertService alertService;

    public SosController(AlertService alertService) {
        this.alertService = alertService;
    }

    /** Page with the 5 signal checkboxes. */
    @GetMapping("/simulate")
    public String simulatePage(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        return "simulate";
    }

    /** Creates the alert from the checked signals. Unchecked boxes are not sent, so default is false. */
    @PostMapping("/sos/trigger")
    public String trigger(@RequestParam(defaultValue = "false") boolean scream,
                          @RequestParam(defaultValue = "false") boolean jerk,
                          @RequestParam(defaultValue = "false") boolean isolated,
                          @RequestParam(defaultValue = "false") boolean lateNight,
                          @RequestParam(defaultValue = "false") boolean heartRate,
                          HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        DangerSignalDto signals = new DangerSignalDto(scream, jerk, isolated, lateNight, heartRate);
        AlertDto alert = alertService.createAlert(userId, signals);
        return "redirect:/sos/alert/" + alert.alertId();
    }

    /** Shows the alert result: countdown, "Are you safe?", or the final status. */
    @GetMapping("/sos/alert/{id}")
    public String alertPage(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        model.addAttribute("alert", alertService.getAlert(userId, id));
        return "alert-result";
    }

    /** Cancel with a PIN. Normal and duress PIN BOTH show "cancelled" to the user. */
    @PostMapping("/sos/cancel/{id}")
    public String cancel(@PathVariable Long id, @RequestParam String pin,
                         HttpSession session, RedirectAttributes redirect) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        try {
            alertService.cancelAlert(userId, id, pin);
            redirect.addFlashAttribute("success", "Alert cancelled.");
            return "redirect:/dashboard";
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/sos/alert/" + id;
        }
    }

    /** Sends the SOS (countdown finished or user pressed Send SOS). */
    @PostMapping("/sos/send/{id}")
    public String send(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        alertService.sendAlert(userId, id);
        return "redirect:/sos/alert/" + id;
    }

    /** Saves "Was this a real emergency?". Real emergency = true means falseAlarm = false. */
    @PostMapping("/sos/feedback/{id}")
    public String feedback(@PathVariable Long id, @RequestParam boolean realEmergency,
                           HttpSession session, RedirectAttributes redirect) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        alertService.submitFeedback(userId, id, !realEmergency);
        redirect.addFlashAttribute("success", "Thanks for your feedback.");
        return "redirect:/sos/alert/" + id;
    }
}
