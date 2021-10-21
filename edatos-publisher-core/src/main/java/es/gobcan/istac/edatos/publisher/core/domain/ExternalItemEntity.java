package es.gobcan.istac.edatos.publisher.core.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;
import org.siemac.edatos.core.common.enume.converter.TypeExternalArtefactsEnumConverter;

import es.gobcan.istac.edatos.publisher.core.domain.interfaces.AbstractVersionedEntity;
import es.gobcan.istac.edatos.publisher.core.domain.vo.InternationalStringVO;

/**
 * External items are entities used in other eDatos services (like structural resources or
 * indicators). This class represents those entities in a generalized way. When a particular
 * external item (like categories) needs a field that is not represented in this class, a new,
 * ad hoc entity is created (see {@link ExternalCategoryEntity}, {@link ExternalOperationEntity}).
 * That entity inherits this one, so all the fields of the external items are available to it,
 * while adding whatever specific field it may need.
 * <p>
 * The inheritance is established though the Hibernate {@link InheritanceType#JOINED} strategy,
 * meaning that the DB has a table for {@link ExternalItemEntity} as well as for every child entity
 * that inherits it. Those child tables only have the fields they declared on the entity, while
 * referencing the parent table though a foreign key.
 * <p>
 * To read more on this, you can check this resources:
 * <ul>
 *   <li><a href="https://www.baeldung.com/hibernate-inheritance">Baeldung: Hibernate Inheritance</a></li>
 *   <li><a href="https://thorben-janssen.com/complete-guide-inheritance-strategies-jpa-hibernate">Thorben Janssen: Inheritance Strategies with JPA and Hibernate</a></li>
 *   <li>High-Performance Java Persistence, Vlad Milhacea, chapter 12.2 (p. 223)</li>
 * </ul>
 */
@Entity
@Table(name = "tb_external_items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
public class ExternalItemEntity extends AbstractVersionedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tb_external_items")
    @SequenceGenerator(name = "seq_tb_external_items", sequenceName = "seq_tb_external_items", allocationSize = 50, initialValue = 1)
    protected Long id;

    @NotNull
    @Column(nullable = false)
    protected String code;

    @NaturalId
    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true, length = 4000)
    protected String urn;

    @NotNull
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    protected InternationalStringVO name;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = TypeExternalArtefactsEnumConverter.class)
    protected TypeExternalArtefactsEnum type;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public InternationalStringVO getName() {
        return name;
    }

    public void setName(InternationalStringVO name) {
        this.name = name;
    }

    public TypeExternalArtefactsEnum getType() {
        return type;
    }

    public void setType(TypeExternalArtefactsEnum type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalItemEntity)) {
            return false;
        }
        ExternalItemEntity that = (ExternalItemEntity) o;
        return Objects.equals(urn, that.urn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urn);
    }
}
