package kz.sdu.roombooking.booking.repository;

import kz.sdu.roombooking.booking.model.RoomSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomSlotRepository extends JpaRepository<RoomSlot, Long> {
}
