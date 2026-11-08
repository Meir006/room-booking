package kz.sdu.roombooking.booking.repository;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import kz.sdu.roombooking.booking.model.RoomSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RoomSlotSpecification {

    public static Specification<RoomSlot> searchAvailable(LocalDate date, LocalTime startTime, LocalTime endTime, String block, Integer floor) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isTrue(root.get("isFree")));

            if (date != null) {
                predicates.add(criteriaBuilder.equal(root.get("date"), date));
            }

            if (block != null && !block.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("block"), block));
            }

            if (floor != null) {
                predicates.add(criteriaBuilder.equal(root.get("floor"), floor));
            }

            if (startTime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startTime)
                );
            }

            if (endTime != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), endTime));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}