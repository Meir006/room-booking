package kz.sdu.roombooking.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kz.sdu.roombooking.booking.dto.BookingPageData;
import kz.sdu.roombooking.booking.dto.RoomSearchCriteria;
import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.service.BookingService;


@Controller 
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    @GetMapping
    public String bookingPage( 
            HttpSession session,
            @ModelAttribute RoomSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/";
        }

        if (page < 0) {
            page = 0;
        }

        BookingPageData data = bookingService.getBookingPageData(userId, criteria, page);

        if (data.activeRequest().isPresent()) {
            return "redirect:/booking/" + data.activeRequest().get().getId();
        }

        model.addAttribute("criteria", criteria);
        model.addAttribute("rooms", data.rooms());

        return "booking";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam Long roomSlotId, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/";
        }

        BookingRequest request = bookingService.createBooking(roomSlotId, userId);

        return "redirect:/booking/" + request.getId();
    }

    @GetMapping("/{id}")
    public String bookingStatus(@PathVariable Long id, Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/";
        }

        BookingRequest request = bookingService.getBookingRequest(id);

        if (!request.getUserId().equals(userId)) {
            return "redirect:/";
        }

        model.addAttribute("request", request);

        return "booking-status";
    }
    
}
