package kz.sdu.roombooking.booking.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import kz.sdu.roombooking.booking.model.BookingStatus;
import kz.sdu.roombooking.booking.repository.BookingRequestRepository;
import kz.sdu.roombooking.booking.repository.RoomSlotRepository;
import kz.sdu.roombooking.booking.repository.RoomSlotSpecification;
import kz.sdu.roombooking.exception.ActiveRequestExistsException;
import kz.sdu.roombooking.exception.ConflictException;
import kz.sdu.roombooking.exception.ResourceNotFoundException;
import kz.sdu.roombooking.exception.UnauthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import kz.sdu.roombooking.booking.dto.RoomSearchCriteria;
import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.model.RoomSlot;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BookingService {
    private static final int PAGE_SIZE = 6;
    private static final int REQUIRED_CONTRIBUTIONS = 5;

    private final RoomSlotRepository roomSlotRepository;
    private final BookingRequestRepository bookingRequestRepository;

    @Transactional(readOnly = true)
    public Page<RoomSlot> searchRooms(
        LocalDate date, LocalTime start,
        LocalTime end, String block,
        Integer floor, Pageable page) {

        Specification<RoomSlot> specification = RoomSlotSpecification.searchAvailable(date, start,
                end, block, floor);

        return roomSlotRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public Page<RoomSlot> loadBookingPage(String userId, RoomSearchCriteria criteria, int page) {
        requireUserId(userId); //changed from requireUserWithoutActiveRequest(userId);

        Pageable pageable = PageRequest.of(Math.max(page, 0), PAGE_SIZE);

        return searchRooms(criteria.getDate(), criteria.getStartTime(),
                criteria.getEndTime(), criteria.getBlock(), criteria.getFloor(), pageable);
    }

    @Transactional
    public BookingRequest createBooking(Long roomSlotId, String userId) {
        requireUserWithoutActiveRequest(userId);

        RoomSlot roomSlot = roomSlotRepository.findById(roomSlotId).orElseThrow(() ->
                new ResourceNotFoundException("Room slot not found " + roomSlotId));

        if (!roomSlot.isFree()){
            throw new ConflictException("Room slot is not free");
        }

        return bookingRequestRepository.save(new BookingRequest(roomSlot, userId));
    }

    @Transactional(readOnly = true)
    public BookingRequest bookingStatus(Long requestId, String userId) {
        requireUserId(userId);

        BookingRequest request = getBookingRequest(requestId);

        if (!request.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Booking request not found: " + requestId);
        }

        return request;
    }


    @Transactional
    public void addContribution(Long requestId) {
        BookingRequest bookingRequest = getBookingRequest(requestId);

        bookingRequest.addContribution();

        if (bookingRequest.getContributions() >= REQUIRED_CONTRIBUTIONS) {
            RoomSlot slot = bookingRequest.getRoomSlot();

            if (!slot.isFree()) {
                throw new ConflictException("This room slot has already been booked");
            }

            bookingRequest.approve();
            slot.setFree(false);
            // reject the other pending requests for this slot here (ADR step)
        }
    }


    private void requireUserId(String userId) {
        if (userId == null) {
            throw new UnauthenticatedException();
        }
    }

    private void requireNoActiveRequest(String userId) {
        getActiveRequest(userId).ifPresent(activeRequest -> {
            throw new ActiveRequestExistsException(activeRequest.getId());
        });
    }

    // the combination used by both "start booking" paths
    private void requireUserWithoutActiveRequest(String userId) {
        requireUserId(userId);
        requireNoActiveRequest(userId);
    } 

    @Transactional(readOnly = true)
    public BookingRequest getBookingRequest(Long requestId) {
        return bookingRequestRepository.findById(requestId).orElseThrow(() ->
                new ResourceNotFoundException("Booking request not found: " + requestId));
    }

    @Transactional(readOnly = true)
    public Optional<BookingRequest> getActiveRequest(String userId) {
        return bookingRequestRepository.findFirstByUserIdAndStatus(userId, BookingStatus.PENDING);
    }
    
}
