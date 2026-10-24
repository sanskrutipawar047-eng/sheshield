package edunet.sheshield_app.controller;

import edunet.sheshield_app.dto.TrustedContactDto;
import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.service.TrustedContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Pages to list, add and delete trusted contacts. */
@Controller
public class TrustedContactController {

    private final TrustedContactService contactService;

    public TrustedContactController(TrustedContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    public String contacts(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        model.addAttribute("contacts", contactService.getContacts(userId));
        return "contacts";
    }

    @PostMapping("/contacts/add")
    public String add(TrustedContactDto dto, HttpSession session, RedirectAttributes redirect) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        try {
            contactService.addContact(userId, dto);
            redirect.addFlashAttribute("success", "Contact added.");
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/contacts";
    }

    @PostMapping("/contacts/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        try {
            contactService.deleteContact(userId, id);
            redirect.addFlashAttribute("success", "Contact deleted.");
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/contacts";
    }
}