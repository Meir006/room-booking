package kz.sdu.roombooking.contribution.event_command.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "processed_events")
@Getter
@NoArgsConstructor
public class ProcessedEvent {

    @Id
    private String eventId;

    public ProcessedEvent(String eventId) {
        this.eventId = eventId;
    }
}