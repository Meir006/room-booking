package kz.sdu.roombooking.booking.state;

import kz.sdu.roombooking.booking.model.BookingRequest;

public class RejectedState implements BookingState {

    @Override
    public void addContribution(BookingRequest request) {
        throw new IllegalStateException("Rejected booking requests cannot accept contributions");
    }

    @Override
    public void approve(BookingRequest request) {
        throw new IllegalStateException("Booking request is already rejected");
    }
}
