package kz.sdu.roombooking.booking;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller 
public class bookingController {
    
    @GetMapping("/booking")
    public String booking() {
        return "booking";
    }
    
}
