package kz.sdu.roombooking.contribution.model;

import jakarta.persistence.*;
import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.exception.ConflictException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contribution_requests",
        uniqueConstraints = {@UniqueConstraint(name = "sender_uc",
        columnNames = {"booking_request_id","receiverId"})})
@Getter
@Setter
@NoArgsConstructor
public class ContributionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_request_id", nullable = false)
    private BookingRequest bookingRequest;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    private ContributionStatus status = ContributionStatus.PENDING;

    public ContributionRequest(BookingRequest bookingRequest, Long senderId, Long receiverId, String message) {
        this.bookingRequest = bookingRequest;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
    }

    public void accept() {
        requirePending();
        status = ContributionStatus.ACCEPTED;
    }

    public void decline() {
        requirePending();
        status = ContributionStatus.DECLINED;
    }

    public boolean isReceivedBy(Long userId) {
        return receiverId.equals(userId);
    }

    private void requirePending() {
        if (status != ContributionStatus.PENDING) {
            throw new ConflictException("Contribution request is already processed");
        }
    }

}
