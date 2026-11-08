package kz.sdu.roombooking.booking.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import kz.sdu.roombooking.booking.model.BookingStatus;
import kz.sdu.roombooking.booking.repository.BookingRequestRepository;
import kz.sdu.roombooking.booking.repository.RoomSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.model.RoomSlot;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final RoomSlotRepository roomSlotRepository;
    private final BookingRequestRepository bookingRequestRepository;

    public Page<RoomSlot> searchRooms(LocalDate date, LocalTime start,
                                      LocalTime end, String block,
                                      Integer floor, Pageable page) {
        return roomSlotRepository.findAvailableSlots(date, start, end, block, floor, page);
    }

    public BookingRequest createBooking(Long roomSlotId, String userId) {
        return null; }

    public BookingRequest getBookingRequest(Long requestId) { return null; }

    public Optional<BookingRequest> getActiveRequest(String userId, BookingStatus status) {
        return bookingRequestRepository.findFirstByUserIdAndStatus(userId, status);
    }
}
