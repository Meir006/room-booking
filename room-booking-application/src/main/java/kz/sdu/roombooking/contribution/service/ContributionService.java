package kz.sdu.roombooking.contribution.service;

import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.booking.service.BookingService;
import kz.sdu.roombooking.contribution.model.ContributionRequest;
import kz.sdu.roombooking.contribution.repository.ContributionRequestRepository;
import kz.sdu.roombooking.exception.ResourceNotFoundException;
import kz.sdu.roombooking.exception.UnauthenticatedException;
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
        ContributionRequest request = getForReceiver(contributionId, userId);

        request.accept();
        bookingService.addContribution(request.getBookingRequest().getId());
    }

    @Transactional
    public void decline(Long contributionId, Long userId) {
        getForReceiver(contributionId, userId).decline();
    }

    private ContributionRequest getForReceiver(Long contributionId, Long userId) {
        ContributionRequest request = contributionRequestRepository.findById(contributionId)
                .orElseThrow(() -> new ResourceNotFoundException("Contribution request not found: " + contributionId));

        if (!request.isReceivedBy(userId)) {
            throw new ResourceNotFoundException("Contribution request not found: " + contributionId);
        }
        return request;
    }

    @Transactional
    public ContributionRequest sendRequest(Long bookingRequestId, Long senderId,
                                           Long receiverId, String message) {
        requireUserId(senderId);
        requireUserId(receiverId);

        BookingRequest bookingRequest = bookingService.bookingStatus(bookingRequestId, senderId);

        return contributionRequestRepository.save(new ContributionRequest(bookingRequest, senderId, receiverId, message));
    }

    private void requireUserId(Long userId) {
        if (userId == null) {
            throw new UnauthenticatedException();
        }
    }

}
