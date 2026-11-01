package kz.sdu.roombooking.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking_requests")
@Getter
@Setter
@NoArgsConstructor
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_slot_id", nullable = false)
    private RoomSlot roomSlot;

    private int contributions = 0;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    public BookingRequest(RoomSlot roomSlot) {
        this.roomSlot = roomSlot;
    }
}