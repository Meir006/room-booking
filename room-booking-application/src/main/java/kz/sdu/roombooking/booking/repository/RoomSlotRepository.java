package kz.sdu.roombooking.booking.repository;

import kz.sdu.roombooking.booking.model.RoomSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RoomSlotRepository extends
        JpaRepository<RoomSlot, Long>,
        JpaSpecificationExecutor<RoomSlot> {
}