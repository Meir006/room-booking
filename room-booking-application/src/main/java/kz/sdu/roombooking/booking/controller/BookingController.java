package kz.sdu.roombooking.booking.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.model.RoomSlot;
import kz.sdu.roombooking.booking.service.BookingService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;


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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalTime startTime,
            @RequestParam(required = false) LocalTime endTime,
            @RequestParam(required = false) String block,
            @RequestParam(required = false) Integer floor,
            Model model) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/";
        }

        Optional<BookingRequest> existing = bookingService.getActiveRequest(userId);

        if (existing.isPresent()) {
            return "redirect:/booking/" + existing.get().getId();
        }

        model.addAttribute("date", date);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("block", block);
        model.addAttribute("floor", floor);

        if (page < 0) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, 10);

        // model.addAttribute("rooms", bookingService.searchRooms(date, startTime, endTime, block, floor, pageable));
        model.addAttribute("rooms", new PageImpl<RoomSlot>(List.of()));

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
