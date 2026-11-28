package kz.sdu.roombooking.contribution.controller;

import jakarta.servlet.http.HttpSession;
import kz.sdu.roombooking.booking.model.BookingRequest;
import kz.sdu.roombooking.contribution.model.ContributionRequest;
import kz.sdu.roombooking.contribution.service.ContributionService;
import kz.sdu.roombooking.exception.UnauthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contribution")
@RequiredArgsConstructor
public class ContributionController {
    private final ContributionService contributionService;

    @GetMapping
    public List<ContributionRequest> getContributionRequests(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new UnauthenticatedException();
        }

        return contributionService.getContributionRequests(userId);
    }

    @PostMapping("/{id}/accept")
    public void acceptContribution(@PathVariable("id") Long contributionId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthenticatedException();
        }
        contributionService.accept(contributionId, userId);
    }

    @PostMapping("/{id}/reject")
    public void rejectContribution(@PathVariable("id") Long contributionId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthenticatedException();
        }
        contributionService.reject(contributionId, userId);
    }
}