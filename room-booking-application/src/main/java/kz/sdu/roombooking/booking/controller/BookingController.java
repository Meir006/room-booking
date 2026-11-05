package kz.sdu.roombooking.booking.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalTime startTime,
            @RequestParam(required = false) LocalTime endTime,
            @RequestParam(required = false) String block,
            @RequestParam(required = false) Integer floor,
            Model model) {
        Optional<BookingRequest> existing = bookingService.getActiveRequest(/* current user ID */);

        if (existing.isPresent()) {
            return "redirect:/booking/" + existing.get().getId();
        }

        model.addAttribute("rooms", bookingService.searchRooms(date, startTime, endTime, block, floor));

        return "booking";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam long roomSlotId) {

        BookingRequest request = bookingService.createBooking(roomSlotId);

        return "redirect:/booking/" + request.getId();
    }

    @GetMapping("/{id}")
    public String bookingStatus(@PathVariable Long id, Model model) {

        BookingRequest request = bookingService.getBookingRequest(id);

        model.addAttribute("request", request);

        return "booking-status";
    }
    
}
