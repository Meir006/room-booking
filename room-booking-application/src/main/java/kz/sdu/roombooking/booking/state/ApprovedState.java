package kz.sdu.roombooking.booking.state;

import kz.sdu.roombooking.booking.model.BookingRequest;

public class ApprovedState implements BookingState {

    @Override
    public void addContribution(BookingRequest request) {
        throw new IllegalStateException(
                "Approved booking requests cannot accept contributions");
    }

    @Override
    public void approve(BookingRequest request) {
        throw new IllegalStateException(
                "Booking request is already approved");
    }
}