package es.gobcan.istac.edatos.publisher.core.service.criteria;

import org.springframework.stereotype.Component;

import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalOperationEntity;
import es.gobcan.istac.edatos.publisher.core.util.CriteriaUtil;

@Component
public class ExternalOperationCriteriaProcessor extends AbstractCriteriaProcessor {

    public static final CriteriaUtil criteriaUtil = new CriteriaUtil(ExternalOperationEntity.class);
    public static final String ENTITY_FIELD_ID = criteriaUtil.validateFieldExists("id");
    public static final String ENTITY_FIELD_URN = criteriaUtil.validateFieldExists("urn");
    public static final String ENTITY_FIELD_CODE = criteriaUtil.validateFieldExists("code");
    public static final String ENTITY_FIELD_ENABLED = criteriaUtil.validateFieldExists("enabled");
    public static final String ENTITY_FIELD_NOTIFICATIONS_ENABLED = criteriaUtil.validateFieldExists("notificationsEnabled");
    public static final String ENTITY_FIELD_PUBLICATION_DATE = criteriaUtil.validateFieldExists("publicationDate");

    public enum QueryProperty {
        ID,
        URN,
        CODE,
        ENABLED,
        NOTIFICATIONS_ENABLED,
        PUBLICATION_DATE,
    }

    public ExternalOperationCriteriaProcessor() {
        super(ExternalOperationEntity.class);
    }

    @Override
    public void registerProcessors() {
        // @formatter:off
        registerRestrictionProcessor(RestrictionProcessorBuilder
                .longRestrictionProcessor()
                .withQueryProperty(QueryProperty.ID).sortable()
                .withEntityProperty(ENTITY_FIELD_ID)
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder
                .stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.URN).sortable()
                .withEntityProperty(ENTITY_FIELD_URN)
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder
                .stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.CODE).sortable()
                .withEntityProperty(ENTITY_FIELD_CODE)
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder
                .booleanRestrictionProcessor()
                .withQueryProperty(QueryProperty.ENABLED).sortable()
                .withEntityProperty(ENTITY_FIELD_ENABLED)
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder
                .booleanRestrictionProcessor()
                .withQueryProperty(QueryProperty.NOTIFICATIONS_ENABLED).sortable()
                .withEntityProperty(ENTITY_FIELD_NOTIFICATIONS_ENABLED)
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder
                .dateRestrictionProcessor()
                .withQueryProperty(QueryProperty.PUBLICATION_DATE).sortable()
                .withEntityProperty(ENTITY_FIELD_PUBLICATION_DATE)
                .build());
        // @formatter:on
    }
}
