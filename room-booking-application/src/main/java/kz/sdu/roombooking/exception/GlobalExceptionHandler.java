package kz.sdu.roombooking.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthenticatedException.class)
    public String handleUnauthenticated() {
        return "redirect:/";
    }

    @ExceptionHandler(ActiveRequestExistsException.class)
    public String handleActiveRequest(ActiveRequestExistsException ex) {
        return "redirect:/booking/" + ex.getRequestId();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "redirect:/"; // 404 page later
    } 

    // Slot taken, lost a race, or the DB constraint fired
    @ExceptionHandler({
            ConflictException.class,
            ObjectOptimisticLockingFailureException.class,
            DataIntegrityViolationException.class
    })
    public String handleConflict(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "That slot is no longer available. Please choose another.");
        return "redirect:/";
    }

}