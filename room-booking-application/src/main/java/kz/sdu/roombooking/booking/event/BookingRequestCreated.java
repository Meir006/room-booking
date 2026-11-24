package kz.sdu.roombooking.booking.event;

import java.time.LocalDate;

public record BookingRequestCreated(Long bookingRequestId, String room, String block,
                                    Integer floor, LocalDate slotDate) {}