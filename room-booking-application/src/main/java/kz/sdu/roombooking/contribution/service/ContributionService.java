package kz.sdu.roombooking.contribution.service;

import kz.sdu.roombooking.booking.service.BookingService;
import kz.sdu.roombooking.contribution.event_command.event.ContributionAccepted;
import kz.sdu.roombooking.contribution.event_command.event.ContributionRejected;
import kz.sdu.roombooking.contribution.event_command.EventPublisher;
import kz.sdu.roombooking.contribution.model.ContributionRequest;
import kz.sdu.roombooking.contribution.model.ContributionStatus;
import kz.sdu.roombooking.contribution.repository.ContributionRequestRepository;
import kz.sdu.roombooking.exception.ConflictException;
import kz.sdu.roombooking.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContributionService {
    private final ContributionRequestRepository contributionRequestRepository;
    private final BookingService bookingService;
    private final EventPublisher eventPublisher;


    @Transactional(readOnly = true)
    public List<ContributionRequest> getContributionRequests(String userId) {
        return contributionRequestRepository.findAllByReceiverId(Long.valueOf(userId));
    }

    @Transactional
    public void accept(Long contributionId, String userId) {
        ContributionRequest contributionRequest = getPendingContributionRequestForReceiver(contributionId, userId);

        contributionRequest.setStatus(ContributionStatus.ACCEPTED);
        contributionRequestRepository.save(contributionRequest);

        bookingService.addContribution(contributionRequest.getBookingRequest().getId());

        eventPublisher.publishContributionAccepted(new ContributionAccepted(
                UUID.randomUUID().toString(),
                contributionId,
                contributionRequest.getBookingRequest().getId(),
                contributionRequest.getSenderId(),
                contributionRequest.getReceiverId(),
                Instant.now()
        ));

    }

    @Transactional
    public void reject(Long contributionId, String userId) {
        ContributionRequest contributionRequest = getPendingContributionRequestForReceiver(contributionId, userId);

        contributionRequest.setStatus(ContributionStatus.DECLINED);
        contributionRequestRepository.save(contributionRequest);

        eventPublisher.publishContributionRejected(new ContributionRejected(
                UUID.randomUUID().toString(),
                contributionId,
                contributionRequest.getBookingRequest().getId(),
                contributionRequest.getSenderId(),
                contributionRequest.getReceiverId(),
                Instant.now()
        ));

    }

    private ContributionRequest getPendingContributionRequestForReceiver(Long contributionRequestId, String userId) {
        ContributionRequest contributionRequest = contributionRequestRepository.findById(contributionRequestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contribution request not found: " + contributionRequestId));

        if (!contributionRequest.getReceiverId().equals(userId)) {
            throw new ResourceNotFoundException("Contribution request not found: " + contributionRequestId);
        }

        if (contributionRequest.getStatus() != ContributionStatus.PENDING) {
            throw new ConflictException("Contribution request is already processed: " + contributionRequestId);
        }

        return contributionRequest;
    }

}
