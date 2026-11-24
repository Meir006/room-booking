package kz.sdu.roombooking.booking.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import kz.sdu.roombooking.booking.event.BookingRequestApproved;
import kz.sdu.roombooking.booking.event.BookingRequestCreated;
import kz.sdu.roombooking.booking.model.BookingStatistic;
import kz.sdu.roombooking.booking.model.StatisticType;
import kz.sdu.roombooking.booking.repository.BookingStatisticsRepository;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class BookingStatisticsListener {
    
    private final BookingStatisticsRepository repository;

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(BookingRequestCreated e) {
        if (repository.existsByBookingRequestIdAndType(e.bookingRequestId(), StatisticType.CREATED)) {
            return;
        }
        repository.save(new BookingStatistic(e.bookingRequestId(), StatisticType.CREATED, e.room(), e.block(), e.floor(), e.slotDate()));
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(BookingRequestApproved e) {
        if (repository.existsByBookingRequestIdAndType(e.bookingRequestId(), StatisticType.APPROVED)) {
            return;
        }
        repository.save(new BookingStatistic(e.bookingRequestId(), StatisticType.APPROVED, e.room(), e.block(), e.floor(), e.slotDate ()));
    }
}
