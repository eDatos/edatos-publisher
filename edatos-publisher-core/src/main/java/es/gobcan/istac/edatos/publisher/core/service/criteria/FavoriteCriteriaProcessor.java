package es.gobcan.istac.edatos.publisher.core.service.criteria;

import org.springframework.stereotype.Component;

import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;

import es.gobcan.istac.edatos.publisher.core.domain.FavoriteEntity;
import es.gobcan.istac.edatos.publisher.core.util.CriteriaUtil;

@Component
public class FavoriteCriteriaProcessor extends AbstractCriteriaProcessor {

    public static final CriteriaUtil criteriaUtil = new CriteriaUtil(FavoriteEntity.class);

    public static final String ENTITY_FIELD_ID = criteriaUtil.validateFieldExists("id");
    public static final String ENTITY_FIELD_USER = criteriaUtil.validateFieldExists("externalUser");
    public static final String ENTITY_FIELD_CREATED_DATE = criteriaUtil.validateFieldExists("createdDate");

    public enum QueryProperty {
        ID,
        EMAIL,
        USER_ID,
        CREATED_DATE,
    }

    public FavoriteCriteriaProcessor() {
        super(FavoriteEntity.class);
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
                .withQueryProperty(QueryProperty.EMAIL)
                .withAlias(ENTITY_FIELD_USER, ENTITY_FIELD_USER)
                .withEntityProperty(ENTITY_FIELD_USER + ".email")
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder
                .longRestrictionProcessor()
                .withQueryProperty(QueryProperty.USER_ID)
                .withAlias(ENTITY_FIELD_USER, ENTITY_FIELD_USER)
                .withEntityProperty(ENTITY_FIELD_USER + ".id")
                .build());
        registerOrderProcessor(OrderProcessorBuilder
                .orderProcessor()
                .withQueryProperty(QueryProperty.EMAIL)
                .withAlias(ENTITY_FIELD_USER, ENTITY_FIELD_USER)
                .withEntityProperty(ENTITY_FIELD_USER + ".email")
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder
                .dateRestrictionProcessor()
                .withQueryProperty(QueryProperty.CREATED_DATE).sortable()
                .withEntityProperty(ENTITY_FIELD_CREATED_DATE)
                .build());
        // @formatter:on
    }
}
