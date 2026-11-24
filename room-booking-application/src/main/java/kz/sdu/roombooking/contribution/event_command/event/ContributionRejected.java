package kz.sdu.roombooking.contribution.event_command.event;

import java.time.Instant;

public record ContributionRejected(
        String eventId,
        Long contributionId,
        Long bookingRequestId,
        String senderId,
        String receiverId,
        Instant occurredAt
) {}