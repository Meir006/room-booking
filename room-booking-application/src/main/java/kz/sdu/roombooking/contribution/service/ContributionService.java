package kz.sdu.roombooking.contribution.service;

import kz.sdu.roombooking.booking.service.BookingService;
import kz.sdu.roombooking.contribution.controller.ContributionController;
import kz.sdu.roombooking.contribution.repository.ContributionRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContributionService {
    private final ContributionController contributionController;
    private final ContributionRequestRepository contributionRequestRepository;
    private final BookingService bookingService;


}
