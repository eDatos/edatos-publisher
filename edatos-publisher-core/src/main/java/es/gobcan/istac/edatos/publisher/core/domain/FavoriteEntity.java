package es.gobcan.istac.edatos.publisher.core.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import es.gobcan.istac.edatos.publisher.core.domain.interfaces.AbstractVersionedAndAuditingEntity;

/**
 * Favorites are references to certain items that the user can designate to be aware of
 * certain events related to them, like creations, modifications, and deletions.
 * <p>
 * In this case, a favorite can be a reference to:
 * <ul>
 * <li>a {@link CategoryEntity}.
 * <li>an {@link ExternalOperationEntity}.
 * </ul>
 */
@Entity
@Table(name = "tb_favorites", uniqueConstraints = {
    // @formatter:off
    @UniqueConstraint(columnNames = {"external_user_fk", "category_fk"}),
    @UniqueConstraint(columnNames = {"external_user_fk", "external_operation_fk"}),
    // @formatter:on
})
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class FavoriteEntity extends AbstractVersionedAndAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tb_favorites")
    @SequenceGenerator(name = "seq_tb_favorites", sequenceName = "seq_tb_favorites", allocationSize = 50, initialValue = 1)
    private Long id;

    @NotNull
    @JoinColumn(name = "external_user_fk", nullable = false)
    @ManyToOne(optional = false)
    private ExternalUserEntity externalUser;

    @ManyToOne
    @JoinColumn(name = "category_fk")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "external_operation_fk")
    private ExternalOperationEntity externalOperation;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExternalUserEntity getExternalUser() {
        return externalUser;
    }

    public void setExternalUser(ExternalUserEntity user) {
        this.externalUser = user;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public ExternalOperationEntity getExternalOperation() {
        return externalOperation;
    }

    public void setExternalOperation(ExternalOperationEntity operation) {
        this.externalOperation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FavoriteEntity)) {
            return false;
        }
        FavoriteEntity that = (FavoriteEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
