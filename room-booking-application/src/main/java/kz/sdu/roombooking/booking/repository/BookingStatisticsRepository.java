package kz.sdu.roombooking.booking.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kz.sdu.roombooking.booking.dto.StatisticsCount;
import kz.sdu.roombooking.booking.model.BookingStatistic;
import kz.sdu.roombooking.booking.model.StatisticType;

public interface BookingStatisticsRepository extends JpaRepository<BookingStatistic, Long> {
        @Query("""
            select new kz.sdu.roombooking.booking.dto.StatisticsCount(s.block, count(s))
            from BookingStatistic s where s.type = :type
            group by s.block order by count(s) desc
            """)
        List<StatisticsCount> countByBlock(@Param("type") StatisticType type);

        @Query("""
            select new kz.sdu.roombooking.booking.dto.StatisticsCount(s.room, count(s))
            from BookingStatistic s where s.type = :type
            group by s.room order by count(s) desc
            """)
        List<StatisticsCount> countByRoom(@Param("type") StatisticType type, Pageable pageable);

        boolean existsByBookingRequestIdAndType(Long bookingRequestId, StatisticType type);
}
