package es.gobcan.istac.edatos.publisher.core.domain.interfaces;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractVersionedAndAuditingAndLogicalDeletionEntity extends AbstractVersionedAndAuditingEntity implements VersionedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "deletion_date")
    private Instant deletionDate;

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Instant getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(Instant deletionDate) {
        this.deletionDate = deletionDate;
    }

    public boolean isDeleted() {
        return deletionDate != null;
    }
}
