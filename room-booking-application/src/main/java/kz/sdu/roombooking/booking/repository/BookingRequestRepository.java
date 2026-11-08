package kz.sdu.roombooking.booking.repository;

import kz.sdu.roombooking.booking.model.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {
}
