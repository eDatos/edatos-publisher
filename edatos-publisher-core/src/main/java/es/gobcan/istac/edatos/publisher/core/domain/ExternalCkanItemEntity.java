package es.gobcan.istac.edatos.publisher.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.NotBlank;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;

@Entity
@Table(name = "tb_ckan_external_items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// jsonb is already defined on a package-level, but for some reason liquibase:diff doesn't detect it, so
// it needs to be declared on at least one entity for it to work. Refer to
// https://stackoverflow.com/a/52117748/7611990
// https://stackoverflow.com/a/56030790/7611990
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@PrimaryKeyJoinColumn(name = "external_item_fk")
public class ExternalCkanItemEntity extends ExternalItemEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    private CkanState ckanState;

    @NotNull
    @NotBlank
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    private String    kafkaMessage;

    private String    ckanId;

    public CkanState getCkanState() {
        return ckanState;
    }

    public void setCkanState(CkanState ckanState) {
        this.ckanState = ckanState;
    }

    public String getKafkaMessage() {
        return kafkaMessage;
    }

    public void setKafkaMessage(String kafkaMessage) {
        this.kafkaMessage = kafkaMessage;
    }

    public String getCkanId() {
        return ckanId;
    }

    public void setCkanId(String ckanId) {
        this.ckanId = ckanId;
    }
}
