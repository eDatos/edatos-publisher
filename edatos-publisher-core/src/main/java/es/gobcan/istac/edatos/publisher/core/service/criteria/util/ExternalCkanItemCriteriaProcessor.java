package es.gobcan.istac.edatos.publisher.core.service.criteria.util;

import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import es.gobcan.istac.edatos.publisher.core.domain.ExternalCkanItemEntity;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;
import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;

public class ExternalCkanItemCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String ENTITY_FIELD_CKAN_STATE = "ckanState";
    private static final String ENTITY_FIELD_KAFKA_MESSAGE = "kafkaMessage";
    private static final String ENTITY_FIELD_CODE = "code";
    private static final String ENTITY_FIELD_URN = "urn";
    private static final String ENTITY_FIELD_NAME = "name";
    private static final String ENTITY_FIELD_TYPE = "type";
    private static final String ENTITY_FIELD_CREATED_BY = "createdBy";
    private static final String ENTITY_FIELD_CREATED_DATE = "createdDate";
    private static final String ENTITY_FIELD_LAST_MODIFIED_BY = "lastModifiedBy";
    private static final String ENTITY_FIELD_LAST_MODIFIED_DATE = "lastModifiedDate";

    public enum QueryProperty {
        CKAN_STATE,
        KAFKA_MESSAGE,
        CODE,
        URN,
        NAME,
        TYPE,
        CREATED_BY,
        CREATED_DATE,
        LAST_MODIFIED_BY,
        LAST_MODIFIED_DATE
    }

    public ExternalCkanItemCriteriaProcessor() {
        super(ExternalCkanItemEntity.class);
    }

    @Override
    public void registerProcessors() {
        //@formatter:off
        registerRestrictionProcessor(RestrictionProcessorBuilder.enumRestrictionProcessor(CkanState.class)
            .withQueryProperty(QueryProperty.CKAN_STATE).sortable()
            .withEntityProperty(ENTITY_FIELD_CKAN_STATE).build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
            .withQueryProperty(QueryProperty.KAFKA_MESSAGE).sortable()
            .withEntityProperty(ENTITY_FIELD_KAFKA_MESSAGE).build());

        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
            .withQueryProperty(QueryProperty.CODE).sortable()
            .withEntityProperty(ENTITY_FIELD_CODE).build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
            .withQueryProperty(QueryProperty.URN).sortable()
            .withEntityProperty(ENTITY_FIELD_URN).build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor() // TODO is international string, not a simple string
            .withQueryProperty(QueryProperty.NAME).sortable()
            .withEntityProperty(ENTITY_FIELD_NAME).build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.enumRestrictionProcessor(TypeExternalArtefactsEnum.class)
            .withQueryProperty(QueryProperty.TYPE).sortable()
            .withEntityProperty(ENTITY_FIELD_TYPE).build());

        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
            .withQueryProperty(QueryProperty.CREATED_BY).sortable()
            .withEntityProperty(ENTITY_FIELD_CREATED_BY).build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.instantRestrictionProcessor()
            .withQueryProperty(QueryProperty.CREATED_DATE).sortable()
            .withEntityProperty(ENTITY_FIELD_CREATED_DATE).build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
            .withQueryProperty(QueryProperty.LAST_MODIFIED_BY).sortable()
            .withEntityProperty(ENTITY_FIELD_LAST_MODIFIED_BY).build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.instantRestrictionProcessor()
            .withQueryProperty(QueryProperty.LAST_MODIFIED_DATE).sortable()
            .withEntityProperty(ENTITY_FIELD_LAST_MODIFIED_DATE).build());
        //@formatter:on
    }
}
