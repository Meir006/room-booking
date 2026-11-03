package kz.sdu.roombooking.contribution;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller 
public class ContributionController {
    
    @GetMapping("/contribution")
    public String contribution() {
        return "contribution";
    }
    
}
