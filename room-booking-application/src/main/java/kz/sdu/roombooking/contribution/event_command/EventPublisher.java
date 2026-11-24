package kz.sdu.roombooking.contribution.event_command;

import kz.sdu.roombooking.contribution.event_command.event.ContributionAccepted;
import kz.sdu.roombooking.contribution.event_command.event.ContributionRejected;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publishContributionAccepted(ContributionAccepted contributionAccepted) {
        eventPublisher.publishEvent(contributionAccepted);
    }

    public void publishContributionRejected(ContributionRejected contributionRejected) {
        eventPublisher.publishEvent(contributionRejected);
    }
}
