package kz.sdu.roombooking.contribution.event_command.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_records")
@Getter
@NoArgsConstructor
public class AuditRecord {

    @Id
    private String userId;

    private int acceptedCount = 0;
    private int rejectedCount = 0;

    public AuditRecord(String userId) {
        this.userId = userId;
    }

    public void incrementAccepted() {
        acceptedCount++;
    }

    public void incrementRejected() {
        rejectedCount++;
    }
}