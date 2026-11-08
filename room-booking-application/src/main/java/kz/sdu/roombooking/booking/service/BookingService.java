package kz.sdu.roombooking.booking.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import kz.sdu.roombooking.booking.model.BookingStatus;
import kz.sdu.roombooking.booking.repository.BookingRequestRepository;
import kz.sdu.roombooking.booking.repository.RoomSlotRepository;
import kz.sdu.roombooking.exception.ConflictException;
import kz.sdu.roombooking.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.model.RoomSlot;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public BookingRequest createBooking(Long roomSlotId, String userId) {
        if(getActiveRequest(userId).isPresent()) {
            throw new ResourceNotFoundException("You already have an active booking request");
        }

        RoomSlot roomSlot = roomSlotRepository.findById(roomSlotId).orElseThrow(() -> new ResourceNotFoundException("Room slot not found" + roomSlotId));

        if(!roomSlot.isFree()){
            throw new ConflictException("Room slot is not free");
        }
        roomSlot.setFree(false);

        return bookingRequestRepository.save(new BookingRequest(roomSlot, userId));
    }



    public BookingRequest getBookingRequest(Long requestId) {
        return bookingRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking request not found: " + requestId));}



    public Optional<BookingRequest> getActiveRequest(String userId) {
        return bookingRequestRepository.findFirstByUserIdAndStatus(userId, BookingStatus.PENDING);
    }
}
