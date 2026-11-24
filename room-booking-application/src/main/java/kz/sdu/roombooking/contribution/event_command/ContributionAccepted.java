package kz.sdu.roombooking.contribution.event_command;

import java.time.Instant;

public record ContributionAccepted(Long eventId, Long contributionId, Long bookingRequestId, Instant occurredAt) {
}
