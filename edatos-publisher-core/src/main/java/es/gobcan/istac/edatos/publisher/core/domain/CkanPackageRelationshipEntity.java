package es.gobcan.istac.edatos.publisher.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_ckan_package_relationship")
public class CkanPackageRelationshipEntity {

    @Id
    private String urn;

    @NotNull
    @Column(unique = true, nullable = false, length = 1000)
    private String ckanId;

    public CkanPackageRelationshipEntity() {
    }

    public CkanPackageRelationshipEntity(String urn, String ckanId) {
        this.urn = urn;
        this.ckanId = ckanId;
    }

    public String getUrn() {
        return urn;
    }

    public String getCkanId() {
        return ckanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CkanPackageRelationshipEntity that = (CkanPackageRelationshipEntity) o;
        return urn.equals(that.urn) && ckanId.equals(that.ckanId);
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (urn != null) {
            result = 31 * result + urn.hashCode();
        }
        if (ckanId != null) {
            result = 31 * result + ckanId.hashCode();
        }
        return result;
    }
}
