package kz.sdu.roombooking.booking.state;

import kz.sdu.roombooking.booking.model.BookingRequest;

public interface BookingState {

    void addContribution(BookingRequest request);

    void approve(BookingRequest request);
}
