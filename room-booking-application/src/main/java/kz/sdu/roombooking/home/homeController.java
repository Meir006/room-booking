package kz.sdu.roombooking.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


@Controller 
public class HomeController {
    
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
    Long userId = (Long) session.getAttribute("userId");

        model.addAttribute("loggedIn", userId != null);
        model.addAttribute("userId", userId);

        return "home";
    }

    @PostMapping("/login")
    public String login(@RequestParam Long userId, HttpSession session) {

        session.setAttribute("userId", userId);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
}
