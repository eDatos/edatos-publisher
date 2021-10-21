package es.gobcan.istac.edatos.publisher.core.service.criteria;

import org.springframework.stereotype.Component;

import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;

import es.gobcan.istac.edatos.publisher.core.domain.CategoryEntity;
import es.gobcan.istac.edatos.publisher.core.util.CriteriaUtil;

@Component
public class CategoryCriteriaProcessor extends AbstractCriteriaProcessor {

    public static final CriteriaUtil criteriaUtil = new CriteriaUtil(CategoryEntity.class);

    public static final String ENTITY_FIELD_ID = criteriaUtil.validateFieldExists("id");

    public enum QueryProperty {
        ID,
    }

    public CategoryCriteriaProcessor() {
        super(CategoryEntity.class);
    }

    @Override
    public void registerProcessors() {
        // @formatter:off
        registerRestrictionProcessor(RestrictionProcessorBuilder
                .longRestrictionProcessor()
                .withQueryProperty(QueryProperty.ID).sortable()
                .withEntityProperty(ENTITY_FIELD_ID)
                .build());
        // @formatter:on
    }
}
