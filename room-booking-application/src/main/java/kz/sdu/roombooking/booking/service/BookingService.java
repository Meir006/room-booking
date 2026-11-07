package kz.sdu.roombooking.booking.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.model.RoomSlot;

@Service
public class BookingService {
    
    public Page<RoomSlot> searchRooms(LocalDate date, LocalTime start, LocalTime end, String block, Integer floor, Pageable page) { return null; }

    public BookingRequest createBooking(Long roomSlotId, String userId) { return null; }

    public BookingRequest getBookingRequest(Long requestId) { return null; }

    public Optional<BookingRequest> getActiveRequest(String userId) { return Optional.empty(); }
}
