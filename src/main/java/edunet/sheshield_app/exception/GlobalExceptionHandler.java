package edunet.sheshield_app.exception;

import java.time.LocalDateTime;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** Catches errors from any controller and shows a friendly error page. */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SheShieldException.class)
    public String handleSheShieldException(SheShieldException ex, Model model) {
        model.addAttribute("error", new ErrorDetails(ex.getMessage(), LocalDateTime.now()));
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherException(Exception ex, Model model) {
        model.addAttribute("error", new ErrorDetails("Something went wrong: " + ex.getMessage(), LocalDateTime.now()));
        return "error";
    }
}