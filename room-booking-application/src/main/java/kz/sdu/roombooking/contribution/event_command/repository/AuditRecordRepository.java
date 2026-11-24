package kz.sdu.roombooking.contribution.event_command.repository;


import kz.sdu.roombooking.contribution.event_command.entity.AuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRecordRepository extends JpaRepository<AuditRecord, String> {
}