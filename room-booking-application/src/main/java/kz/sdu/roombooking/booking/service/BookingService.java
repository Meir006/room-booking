package kz.sdu.roombooking.booking.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.model.RoomSlot;

public class BookingService {

    
    public List<RoomSlot> searchRooms(LocalDate date, LocalTime start, LocalTime end, String block, Integer floor) { return null; }

    public BookingRequest createBooking(long roomSlotId) { return null; }

    public BookingRequest getBookingRequest(long requestId) { return null; }

    public Optional<BookingRequest> getActiveRequest(/* id here later */) { return Optional.empty(); }
}
