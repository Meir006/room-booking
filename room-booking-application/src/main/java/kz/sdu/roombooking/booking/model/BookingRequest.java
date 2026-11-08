package kz.sdu.roombooking.booking.model;

import jakarta.persistence.*;
import kz.sdu.roombooking.booking.state.ApprovedState;
import kz.sdu.roombooking.booking.state.BookingState;
import kz.sdu.roombooking.booking.state.PendingState;
import kz.sdu.roombooking.booking.state.RejectedState;
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

    @Column(nullable = false)
    private String userId;

    private int contributions = 0;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @Transient
    private BookingState state = new PendingState();

    @PostLoad
    private void initializeState() {
        state = switch (status) {
            case PENDING -> new PendingState();
            case APPROVED -> new ApprovedState();
            case REJECTED -> new RejectedState();
        };
    }

    public BookingRequest(RoomSlot roomSlot, String userId) {
        this.roomSlot = roomSlot;
        this.userId = userId;
    }

    public void addContribution() {
        state.addContribution(this);
    }

    public void approve() {
        state.approve(this);
    }
}