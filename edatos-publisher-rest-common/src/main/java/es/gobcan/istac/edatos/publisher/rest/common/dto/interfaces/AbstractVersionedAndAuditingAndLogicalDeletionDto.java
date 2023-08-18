package es.gobcan.istac.edatos.publisher.rest.common.dto.interfaces;

import java.time.Instant;

public abstract class AbstractVersionedAndAuditingAndLogicalDeletionDto extends AbstractVersionedAndAuditingDto {

    private String deletedBy;
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
}
