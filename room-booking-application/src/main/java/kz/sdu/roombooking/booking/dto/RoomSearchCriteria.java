package kz.sdu.roombooking.booking.dto;

import java.time.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RoomSearchCriteria {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String block;
    private Integer floor;
}