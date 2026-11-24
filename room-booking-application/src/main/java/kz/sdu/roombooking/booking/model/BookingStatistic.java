package kz.sdu.roombooking.booking.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking_statistics")
@Getter
@NoArgsConstructor
public class BookingStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingRequestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatisticType type;

    private String room;
    private String block;
    private Integer floor;
    private LocalDate slotDate;

    @Column(nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    public BookingStatistic(Long bookingRequestId, StatisticType type, String room, String block,
                            Integer floor, LocalDate slotDate) {
        this.bookingRequestId = bookingRequestId;
        this.type = type;
        this.room = room;
        this.block = block;
        this.floor = floor;
        this.slotDate = slotDate;
    }
}