package kz.sdu.roombooking.booking.repository;

import kz.sdu.roombooking.booking.model.RoomSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoomSlotRepository extends
        JpaRepository<RoomSlot, Long>,
        JpaSpecificationExecutor<RoomSlot> {
}