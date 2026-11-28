package kz.sdu.roombooking.contribution.service;

import kz.sdu.roombooking.booking.service.BookingService;
import kz.sdu.roombooking.contribution.model.ContributionRequest;
import kz.sdu.roombooking.contribution.model.ContributionStatus;
import kz.sdu.roombooking.contribution.repository.ContributionRequestRepository;
import kz.sdu.roombooking.exception.ConflictException;
import kz.sdu.roombooking.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributionService {
    private final ContributionRequestRepository contributionRequestRepository;
    private final BookingService bookingService;


    @Transactional(readOnly = true)
    public List<ContributionRequest> getContributionRequests(Long userId) {
        return contributionRequestRepository.findAllByReceiverId(userId);
    }

    @Transactional
    public void accept(Long contributionId, Long userId) {
        ContributionRequest contributionRequest = getPendingContributionRequestForReceiver(contributionId, userId);

        contributionRequest.setStatus(ContributionStatus.ACCEPTED);
        contributionRequestRepository.save(contributionRequest);

        bookingService.addContribution(contributionRequest.getBookingRequest().getId());
    }

    @Transactional
    public void reject(Long contributionId, Long userId) {
        ContributionRequest contributionRequest = getPendingContributionRequestForReceiver(contributionId, userId);

        contributionRequest.setStatus(ContributionStatus.DECLINED);
        contributionRequestRepository.save(contributionRequest);
    }

    private ContributionRequest getPendingContributionRequestForReceiver(Long contributionRequestId, Long userId) {
        ContributionRequest contributionRequest = contributionRequestRepository.findById(contributionRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("contribution request not found" + userId));

        if(!contributionRequest.getReceiverId().equals(userId)) {
            throw new ResourceNotFoundException("contribution request not found not found: " + userId);
        }

        if(contributionRequest.getStatus() != ContributionStatus.PENDING){
            throw new ConflictException("contribution request is already processed: ");
        }
        return contributionRequest;
    }

}
