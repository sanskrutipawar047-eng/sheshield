package edunet.sheshield_app.controller;

import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.service.TripService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Pages to start, end and list trips. */
@Controller
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/trips")
    public String trips(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        model.addAttribute("trips", tripService.getTrips(userId));
        return "trips";
    }

    @PostMapping("/trips/start")
    public String start(@RequestParam String destination, HttpSession session, RedirectAttributes redirect) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        try {
            tripService.startTrip(userId, destination);
            redirect.addFlashAttribute("success", "Trip started. Your trusted contacts can follow it.");
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trips";
    }

    @PostMapping("/trips/end/{id}")
    public String end(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        try {
            tripService.endTrip(userId, id);
            redirect.addFlashAttribute("success", "Trip completed. Glad you reached safely.");
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trips";
    }
}