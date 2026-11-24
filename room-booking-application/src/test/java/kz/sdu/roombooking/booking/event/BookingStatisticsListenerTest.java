package kz.sdu.roombooking.booking.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kz.sdu.roombooking.booking.listener.BookingStatisticsListener;
import kz.sdu.roombooking.booking.model.BookingStatistic;
import kz.sdu.roombooking.booking.model.StatisticType;
import kz.sdu.roombooking.booking.repository.BookingStatisticsRepository;

@DataJpaTest
@Import(BookingStatisticsListener.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookingStatisticsListenerTest {

    @Autowired
    private BookingStatisticsListener listener;

    @Autowired
    private BookingStatisticsRepository repository;

    @BeforeEach
    void cleanTable() {
        repository.deleteAll();
    }

    @Test
    void savesCreatedStatisticFromEvent() {
        var event = new BookingRequestCreated(7L, "A101", "A", 1, LocalDate.of(2026, 12, 1));

        listener.on(event);

        List<BookingStatistic> all = repository.findAll();
        assertEquals(1, all.size());

        BookingStatistic saved = all.get(0);
        assertEquals(StatisticType.CREATED, saved.getType());
        assertEquals(7L, saved.getBookingRequestId());
        assertEquals("A101", saved.getRoom());
        assertEquals("A", saved.getBlock());
        assertEquals(1, saved.getFloor());
        assertEquals(LocalDate.of(2026, 12, 1), saved.getSlotDate());
        assertNotNull(saved.getRecordedAt());

        System.out.println("\n\nSUCCESS: Booking request statistic created\n\n");
    }

    @Test
    void savesApprovedStatisticFromEvent() {
        var event = new BookingRequestApproved(9L, "B202", "B", 2, LocalDate.of(2026, 12, 2));

        listener.on(event);

        List<BookingStatistic> all = repository.findAll();
        assertEquals(1, all.size());

        BookingStatistic saved = all.get(0);
        assertEquals(StatisticType.APPROVED, saved.getType());
        assertEquals(9L, saved.getBookingRequestId());
        assertEquals("B202", saved.getRoom());

        System.out.println("\n\nSUCCESS: Approved request statistic created\n\n");
    }

    @Test
    void sameEventHandledTwiceSavesOneStatistic() {
        var event = new BookingRequestCreated(7L, "A101", "A", 1, LocalDate.of(2026, 12, 1));

        listener.on(event);
        listener.on(event);

        assertEquals(1, repository.count());

        System.out.println("\n\nSUCCESS: No duplicate on double even handling created\n\n");
    }

    @Test
    void createdAndApprovedForSameRequestAreBothKept() {
        listener.on(new BookingRequestCreated(7L, "A101", "A", 1, LocalDate.of(2026, 12, 1)));
        listener.on(new BookingRequestApproved(7L, "A101", "A", 1, LocalDate.of(2026, 12, 1)));

        assertEquals(2, repository.count());

        System.out.println("\n\nSUCCESS: Both created and approved statistics are saved\n\n");
    }
}