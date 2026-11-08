package kz.sdu.roombooking.booking.state;

import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.model.BookingStatus;

public class PendingState implements BookingState {

    @Override
    public void addContribution(BookingRequest request) {
        request.setContributions(request.getContributions() + 1);
    }

    @Override
    public void approve(BookingRequest request) {
        request.setStatus(BookingStatus.APPROVED);
        request.setState(new ApprovedState());
    }
}