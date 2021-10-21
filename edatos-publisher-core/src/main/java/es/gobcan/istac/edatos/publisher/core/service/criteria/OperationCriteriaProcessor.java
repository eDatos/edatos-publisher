package es.gobcan.istac.edatos.publisher.core.service.criteria;

import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalOperationEntity;

public class OperationCriteriaProcessor extends AbstractCriteriaProcessor {

    public enum QueryProperty {
        ID,
        CODE,
        NAME,
        CATEGORY_ID,
    }

    public OperationCriteriaProcessor() {
        super(ExternalOperationEntity.class);
    }

    @Override
    public void registerProcessors() {
        //@formatter:off

        // When the ID is specified, a search by CODE must be performed.
        // In the internal and external API the search by ID correspond to a search by CODE.
        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.ID).sortable()
                .withEntityProperty(ExternalOperationEntity.Properties.CODE).build());
        registerOrderProcessor(
                OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.ID)
                .withEntityProperty(ExternalOperationEntity.Properties.CODE)
                .build());

        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.CODE).sortable()
                .withEntityProperty(ExternalOperationEntity.Properties.CODE).build());
        registerOrderProcessor(
                OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.CODE)
                .withEntityProperty(ExternalOperationEntity.Properties.CODE)
                .build());

        // TODO(EDATOS-3294): Criteria processors don't work with json/jsonb fields.
        registerOrderProcessor(
                OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.NAME)
                .withEntityProperty(ExternalOperationEntity.Properties.NAME)
                .build());

        registerRestrictionProcessor(RestrictionProcessorBuilder.longRestrictionProcessor()
                .withQueryProperty(QueryProperty.CATEGORY_ID)
                .withAlias(ExternalOperationEntity.Properties.CATEGORY, ExternalOperationEntity.Properties.CATEGORY)
                .withEntityProperty(ExternalOperationEntity.Properties.CATEGORY + ".id")
                .build());
        //@formatter:on
    }
}
