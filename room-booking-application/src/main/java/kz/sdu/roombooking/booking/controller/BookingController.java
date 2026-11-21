package kz.sdu.roombooking.booking.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;    
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kz.sdu.roombooking.booking.dto.RoomSearchCriteria;
import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.model.RoomSlot;
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

        Page<RoomSlot> roomSlots = bookingService.loadBookingPage(userId, criteria, page);

        model.addAttribute("criteria", criteria);
        model.addAttribute("rooms", roomSlots);

        return "booking";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam Long roomSlotId, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        BookingRequest request = bookingService.createBooking(roomSlotId, userId);

        return "redirect:/booking/" + request.getId();
    }

    @GetMapping("/{requestId}")
    public String bookingStatus(@PathVariable Long requestId, Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        BookingRequest request = bookingService.bookingStatus(requestId, userId);

        model.addAttribute("request", request);

        return "booking-status";
    }
    
}
