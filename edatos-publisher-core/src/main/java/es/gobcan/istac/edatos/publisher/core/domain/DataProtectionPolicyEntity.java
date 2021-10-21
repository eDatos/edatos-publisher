package es.gobcan.istac.edatos.publisher.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import es.gobcan.istac.edatos.publisher.core.domain.interfaces.AbstractVersionedAndAuditingEntity;
import es.gobcan.istac.edatos.publisher.core.domain.vo.InternationalStringVO;

/**
 * The data protection policy is a document that describes how and for what the user's personal data is used.
 */
@Entity
@Table(name = "tb_data_protection_policy")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class DataProtectionPolicyEntity extends AbstractVersionedAndAuditingEntity {

    private static final long serialVersionUID = 4368946482911324134L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tb_data_protection_policy")
    @SequenceGenerator(name = "seq_tb_data_protection_policy", sequenceName = "seq_tb_data_protection_policy", allocationSize = 50, initialValue = 1)
    private Long id;

    /**
     * The multi-language content of the data protection policy. Rich text is supported.
     */
    @NotNull
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    private InternationalStringVO value;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InternationalStringVO getValue() {
        return value;
    }

    public void setValue(InternationalStringVO value) {
        this.value = value;
    }
}
