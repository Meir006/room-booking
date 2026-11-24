package kz.sdu.roombooking.booking.event;

import java.time.LocalDate;

public record BookingRequestApproved(Long bookingRequestId, String room, String block,
                                     Integer floor, LocalDate slotDate) {}