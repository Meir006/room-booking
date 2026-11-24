package kz.sdu.roombooking.contribution.repository;

import kz.sdu.roombooking.contribution.model.ContributionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.crypto.Cipher;
import java.util.List;

public interface ContributionRequestRepository extends JpaRepository<ContributionRequest, Long> {
        List<ContributionRequest> findAllByReceiverId(String receiverId);
}
