package kz.sdu.roombooking.booking.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RoomSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String room;
    private String block;
    private int floor;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private boolean isFree = true;

    public RoomSlot(String room, LocalDate date,
                    LocalTime startTime, LocalTime endTime) {
        this.room = room;
        this.block = room.substring(0, 1);
        this.floor = Character.digit(room.charAt(1), 10);

        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}