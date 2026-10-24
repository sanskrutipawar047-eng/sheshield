package edunet.sheshield_app.controller;

import edunet.sheshield_app.exception.SheShieldException;
import edunet.sheshield_app.service.EvidenceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Pages for the evidence vault: list, upload, verify integrity. */
@Controller
public class EvidenceController {

    private final EvidenceService evidenceService;

    public EvidenceController(EvidenceService evidenceService) {
        this.evidenceService = evidenceService;
    }

    @GetMapping("/evidence")
    public String evidence(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        model.addAttribute("files", evidenceService.getEvidence(userId));
        return "evidence";
    }

    @PostMapping("/evidence/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         HttpSession session, RedirectAttributes redirect) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        try {
            evidenceService.upload(userId, file);
            redirect.addFlashAttribute("success", "Evidence encrypted and saved in the vault.");
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/evidence";
    }

    @PostMapping("/evidence/verify/{id}")
    public String verify(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        try {
            if (evidenceService.verifyIntegrity(userId, id)) {
                redirect.addFlashAttribute("success", "Integrity verified. The file is unchanged.");
            } else {
                redirect.addFlashAttribute("error", "WARNING: the file is missing or has been tampered with!");
            }
        } catch (SheShieldException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/evidence";
    }
}