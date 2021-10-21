package es.gobcan.istac.edatos.publisher.core.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.TypeDef;
import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

/**
 * Categories are commonly known as 'subjects' or 'areas'. They represent a standardized way to group together other
 * semantic related subjects or statistical operations. In EDatos the categories are handled through the
 * structural resources manager.
 * <p>
 * This class uses a subset of attributes extracted from
 * <p>
 * {@code metamac-srm-core:org.siemac.metamac.srm.core.category.domain.CategoryMetamac}.
 */
@Entity
@Table(name = "tb_external_categories")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// jsonb is already defined on a package-level, but for some reason liquibase:diff doesn't detect it, so
// it needs to be declared on at least one entity for it to work. Refer to
// https://stackoverflow.com/a/52117748/7611990
// https://stackoverflow.com/a/56030790/7611990
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@PrimaryKeyJoinColumn(name="external_item_fk")
public class ExternalCategoryEntity extends ExternalItemEntity {

    public ExternalCategoryEntity() {
        type = TypeExternalArtefactsEnum.CATEGORY;
    }

    /**
     * The nested code splits the codes from all the levels by a dot. For example: 060.060_010.060_010_030.
     */
    private String nestedCode;

    public String getNestedCode() {
        return nestedCode;
    }

    public void setNestedCode(String nestedCode) {
        this.nestedCode = nestedCode;
    }
}
