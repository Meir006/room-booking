package kz.sdu.roombooking.contribution.event_command;

import kz.sdu.roombooking.contribution.event_command.entity.AuditRecord;
import kz.sdu.roombooking.contribution.event_command.entity.ProcessedEvent;
import kz.sdu.roombooking.contribution.event_command.event.ContributionAccepted;
import kz.sdu.roombooking.contribution.event_command.event.ContributionRejected;
import kz.sdu.roombooking.contribution.event_command.repository.AuditRecordRepository;
import kz.sdu.roombooking.contribution.event_command.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class ContributionAuditListener {
    private final AuditRecordRepository auditRecordRepository;
    private final ProcessedEventRepository processedEventRepository;

    @EventListener
    @Transactional
    public void handle(ContributionAccepted event) {
        if (processedEventRepository.existsById(event.eventId())) {
            return;
        }

        AuditRecord auditRecord = getOrCreate(event.receiverId());
        auditRecord.incrementAccepted();

        auditRecordRepository.save(auditRecord);
        processedEventRepository.save(new ProcessedEvent(event.eventId()));
    }

    @EventListener
    @Transactional
    public void handle(ContributionRejected event) {
        if (processedEventRepository.existsById(event.eventId())) {
            return;
        }

        AuditRecord auditRecord = getOrCreate(event.receiverId());
        auditRecord.incrementRejected();

        auditRecordRepository.save(auditRecord);
        processedEventRepository.save(new ProcessedEvent(event.eventId()));
    }

    private AuditRecord getOrCreate(String userId) {
        return auditRecordRepository.findById(userId)
                .orElseGet(() -> new AuditRecord(userId));
    }
}