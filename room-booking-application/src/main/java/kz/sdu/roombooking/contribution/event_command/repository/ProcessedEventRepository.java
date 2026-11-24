package kz.sdu.roombooking.contribution.event_command.repository;

import kz.sdu.roombooking.contribution.event_command.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {
}
