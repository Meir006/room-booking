package kz.sdu.roombooking.contribution.event_command;

import java.time.Instant;

public record ContributionAccepted(
        String eventId,
        Long contributionId,
        Long bookingRequestId,
        String senderId,
        String receiverId,
        Instant occurredAt
) {}
