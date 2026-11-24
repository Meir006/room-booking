package kz.sdu.roombooking.booking.service;

import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.contribution.event_command.ContributionAuditListener;
import kz.sdu.roombooking.contribution.event_command.EventPublisher;
import kz.sdu.roombooking.contribution.event_command.entity.AuditRecord;
import kz.sdu.roombooking.contribution.event_command.entity.ProcessedEvent;
import kz.sdu.roombooking.contribution.event_command.event.ContributionAccepted;
import kz.sdu.roombooking.contribution.event_command.repository.AuditRecordRepository;
import kz.sdu.roombooking.contribution.event_command.repository.ProcessedEventRepository;
import kz.sdu.roombooking.contribution.model.ContributionRequest;
import kz.sdu.roombooking.contribution.model.ContributionStatus;
import kz.sdu.roombooking.contribution.repository.ContributionRequestRepository;
import kz.sdu.roombooking.contribution.service.ContributionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventCommandTest {

    @Mock
    private ContributionRequestRepository contributionRequestRepository;
    @Mock
    private BookingService bookingService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private AuditRecordRepository auditRecordRepository;
    @Mock
    private ProcessedEventRepository processedEventRepository;

    @InjectMocks
    private ContributionService contributionService;

    private ContributionAuditListener listener;

    @BeforeEach
    void setUp() {
        listener = new ContributionAuditListener(auditRecordRepository, processedEventRepository);
    }

    @Test
    void successfulReaction() {
        BookingRequest booking = new BookingRequest();
        booking.setId(10L);

        ContributionRequest contribution = new ContributionRequest();
        contribution.setId(1L);
        contribution.setBookingRequest(booking);
        contribution.setSenderId("sender");
        contribution.setReceiverId("receiver");
        contribution.setStatus(ContributionStatus.PENDING);

        when(contributionRequestRepository.findById(1L)).thenReturn(Optional.of(contribution));

        contributionService.accept(1L, "receiver");

        // 1. main operation succeeded
        assertEquals(ContributionStatus.ACCEPTED, contribution.getStatus());

        // 2. event published
        ArgumentCaptor<ContributionAccepted> eventCaptor = ArgumentCaptor.forClass(ContributionAccepted.class);
        verify(eventPublisher).publishContributionAccepted(eventCaptor.capture());

        // 3. consumer effect
        when(auditRecordRepository.findById("receiver")).thenReturn(Optional.empty());
        listener.handle(eventCaptor.getValue());

        ArgumentCaptor<AuditRecord> recordCaptor = ArgumentCaptor.forClass(AuditRecord.class);
        verify(auditRecordRepository).save(recordCaptor.capture());
        assertEquals(1, recordCaptor.getValue().getAcceptedCount());
    }

    @Test
    void duplicateDeliveryHasEffectOnlyOnce() {
        Set<String> processedIds = new HashSet<>();
        Map<String, AuditRecord> records = new HashMap<>();

        when(processedEventRepository.existsById(anyString()))
                .thenAnswer(invocation -> processedIds.contains(invocation.<String>getArgument(0)));
        when(processedEventRepository.save(any(ProcessedEvent.class)))
                .thenAnswer(invocation -> {
                    ProcessedEvent processed = invocation.getArgument(0);
                    processedIds.add(processed.getEventId());
                    return processed;
                });
        when(auditRecordRepository.findById(anyString()))
                .thenAnswer(invocation -> Optional.ofNullable(records.get(invocation.<String>getArgument(0))));
        when(auditRecordRepository.save(any(AuditRecord.class)))
                .thenAnswer(invocation -> {
                    AuditRecord record = invocation.getArgument(0);
                    records.put(record.getUserId(), record);
                    return record;
                });

        ContributionAccepted event =
                new ContributionAccepted("event-1", 1L, 10L, "sender", "receiver", Instant.now());

        listener.handle(event);
        listener.handle(event);

        assertEquals(1, records.get("receiver").getAcceptedCount());
        assertEquals(1, processedIds.size());
    }
}