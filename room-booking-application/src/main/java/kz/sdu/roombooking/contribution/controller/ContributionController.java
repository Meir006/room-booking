package kz.sdu.roombooking.contribution.controller;

import jakarta.servlet.http.HttpSession;
import kz.sdu.roombooking.contribution.model.ContributionRequest;
import kz.sdu.roombooking.contribution.service.ContributionService;
import kz.sdu.roombooking.exception.UnauthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contribution")
@RequiredArgsConstructor
public class ContributionController {
    private final ContributionService contributionService;

    @GetMapping
    public List<ContributionRequest> getContributionRequests(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            throw new UnauthenticatedException();
        }

        return contributionService.getContributionRequests(userId);
    }
}