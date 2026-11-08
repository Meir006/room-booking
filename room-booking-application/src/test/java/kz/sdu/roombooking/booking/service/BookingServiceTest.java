package kz.sdu.roombooking.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.model.BookingStatus;
import kz.sdu.roombooking.booking.model.RoomSlot;
import kz.sdu.roombooking.booking.repository.BookingRequestRepository;
import kz.sdu.roombooking.booking.repository.RoomSlotRepository;
import kz.sdu.roombooking.exception.ConflictException;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRequestRepository bookingRequestRepository;

    @Mock
    private RoomSlotRepository roomSlotRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createPendingBooking() {
        RoomSlot room = new RoomSlot();
        room.setId(1L);
        room.setFree(true);

        when(roomSlotRepository.findById(1L)).thenReturn(Optional.of(room));

        when(bookingRequestRepository.findFirstByUserIdAndStatus(anyString(), any())).thenReturn(Optional.empty());

        when(bookingRequestRepository.save(any(BookingRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingRequest result = bookingService.createBooking(1L, "student123");

        assertEquals(BookingStatus.PENDING, result.getStatus());
        assertEquals("student123", result.getUserId());

        System.out.println("\n\nSUCCESS: Pending booking created successfully! ");
    }

    @Test
    void rejectBookingWhenUserHasActiveRequest() {
        RoomSlot room = new RoomSlot();
        room.setId(1L);
        room.setFree(true);

        BookingRequest existingRequest = new BookingRequest(room, "student123");

        when(bookingRequestRepository.findFirstByUserIdAndStatus(anyString(), any())).thenReturn(Optional.of(existingRequest));

        assertThrows(IllegalStateException.class, () -> bookingService.createBooking(1L, "student123"));

        verify(bookingRequestRepository, never()).save(any(BookingRequest.class));

        System.out.println("\n\nSUCCESS: Booking rejected when user already has a booking");
    }

    @Test
    void shouldRejectBookingWhenRoomSlotIsNotFree() {
        RoomSlot room = new RoomSlot();
        room.setFree(false);

        when(roomSlotRepository.findById(1L)).thenReturn(Optional.of(room));

        when(bookingRequestRepository.findFirstByUserIdAndStatus(anyString(), any())).thenReturn(Optional.empty());

        assertThrows(ConflictException.class, () -> bookingService.createBooking(1L, "student123"));

        verify(bookingRequestRepository, never()).save(any(BookingRequest.class));

        System.out.println("\n\nSUCCESS: Booking rejected when the room is already taken");
    }
}