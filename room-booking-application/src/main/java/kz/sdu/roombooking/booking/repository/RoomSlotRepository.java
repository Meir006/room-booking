package kz.sdu.roombooking.booking.repository;

import kz.sdu.roombooking.booking.model.RoomSlot;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RoomSlotRepository extends JpaRepository<RoomSlot, Long> {
    @Query("SELECT s FROM RoomSlot s WHERE s.date = :date " +
            "AND s.block = :block " +
            "AND s.floor = :floor " +
            "AND s.startTime >= :startTime " +
            "AND s.endTime <= :endTime")
    List<RoomSlot> findAvailableSlots(
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("block") String block,
            @Param("floor") Integer floor,
            Pageable pageable
    );
}
