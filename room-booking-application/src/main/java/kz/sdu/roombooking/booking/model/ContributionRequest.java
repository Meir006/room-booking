package kz.sdu.roombooking.booking.model;

import jakarta.persistence.*;
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
    private String senderId;

    @Column(nullable = false)
    private String receiverId;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    private ContributionStatus status = ContributionStatus.PENDING;


}
