package kz.sdu.roombooking.contribution.repository;

import kz.sdu.roombooking.contribution.model.ContributionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributionRequestRepository extends JpaRepository<ContributionRequest, Long> {
}
