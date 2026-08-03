package es.gobcan.istac.edatos.publisher.service.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.TimestampType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.ProcStatusEnumAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.config.properties.KafkaProperties;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCkanItemService;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.ExternalCkanItemMapper;
import es.gobcan.istac.edatos.publisher.service.KafkaConsumerService;
import es.ibestat.jaxi.stream.messages.DatasetAvro;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private final Logger                  log                    = LoggerFactory.getLogger(KafkaConsumerServiceImpl.class);
    private static final String           WHITHOUT_CKAN_ID_ERROR = " without ckanId";
    private final ExternalCkanItemMapper  externalCkanItemMapper;
    private final ExternalCkanItemService externalCkanItemService;
    private final ConsumerProcessFacade   consumerProcessFacade;

    public KafkaConsumerServiceImpl(ConsumerProcessFacade consumerProcessFacade, ExternalCkanItemService externalCkanItemService, ExternalCkanItemMapper externalCkanItemMapper) {
        this.consumerProcessFacade = consumerProcessFacade;
        this.externalCkanItemService = externalCkanItemService;
        this.externalCkanItemMapper = externalCkanItemMapper;
    }

    @KafkaListener(id = KafkaProperties.GROUP_ID + "-datasetPublicationTopicId", groupId = KafkaProperties.GROUP_ID, topics = "#{kafkaProperties.getDatasetPublicationTopic()}")
    public void processDatasetPublicationEvent(ConsumerRecord<String, DatasetVersionAvro> consumerRecord) {
        DatasetVersionAvro datasetVersion = consumerRecord.value();

        // Reload messages are intended only for the search indexer; ignore them here.
        if (datasetVersion.getSiemacMetadataStatisticalResource()
                          .getLifecycleStatisticalResource()
                          .getVersionableStatisticalResource()
                          .getReload()) {
            log.info("Publisher. Ignoring dataset {} because reload flag is true", consumerRecord.key());
            return;
        }

        PublicationConsumerProcessResult result = consumerProcessFacade.processDatasetPublicationMessage(datasetVersion);
        try {
            externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(datasetVersion, result.getStatus(), result.getCkanId()));
        } catch (Exception e) {
            log.error("ERROR. dataset metadata were not saved in externalCkanItem  ckanId:" + (result.getCkanId() != null ? result.getCkanId() : WHITHOUT_CKAN_ID_ERROR), e);
        }

    }

    @KafkaListener(id = KafkaProperties.GROUP_ID + "-externalPublicationTopicId", groupId = KafkaProperties.GROUP_ID, topics = "#{kafkaProperties.getDatasetExternalPublicationTopic()}")
    public void processJaxiDatasetPublicationEvent(ConsumerRecord<String, DatasetAvro> consumerRecord) {
        logJaxi(consumerRecord);
        DatasetAvro jaxiDataset = consumerRecord.value();
        PublicationConsumerProcessResult result = consumerProcessFacade.processJaxiDatasetPublicationMessage(jaxiDataset);

        try {
            externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(jaxiDataset, result.getStatus(), result.getCkanId()));
        } catch (Exception e) {
            log.error("ERROR. jaxi dataset metadata were not saved in externalCkanItem ckanId:" + (result.getCkanId() != null ? result.getCkanId() : WHITHOUT_CKAN_ID_ERROR), e);
        }

    }

    private void logJaxi(ConsumerRecord<String, DatasetAvro> consumerRecord) {
        try {
            String topicName = "JAXI_PUBLICATIONS";
            String message = "Received jaxi message from Kafka in publisher -> Topic Name: ";
            TimestampType timestampType = consumerRecord.timestampType();
            long timestamp = consumerRecord.timestamp();
            StringBuilder logMessageBldr = new StringBuilder(message);
            logMessageBldr.append(topicName).append(", Partition: ").append(consumerRecord.partition()).append(", Offset: ").append(consumerRecord.offset()).append(", TimestampType: ")
                    .append(timestampType).append(", Timestamp: ").append(timestamp).append(" [").append(new DateTime(timestamp, DateTimeZone.forID("Atlantic/Canary"))).append("]");
            log.info(logMessageBldr.toString());
        } catch (Exception e) {
            log.error("ERROR IN PUBLISHER. jaxi dataset metadata were not saved in externalCkanItem because and unknown error");
        }

    }

    @KafkaListener(id = KafkaProperties.GROUP_ID + "-codelistsPublicationTopicId", groupId = KafkaProperties.GROUP_ID, topics = "#{kafkaProperties.getCodelistsPublicationTopic()}")
    public void processCodelistPublicationEvent(ConsumerRecord<String, CodelistAvro> consumerRecord) {
        CodelistAvro codelist = consumerRecord.value();
        if (checkCanBePublished(codelist.getProcStatus(), codelist.getLatestVersionNumberPublic())) {
            PublicationConsumerProcessResult result = consumerProcessFacade.processCodelistPublicationMessage(codelist);
            try {
                externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(codelist, result.getStatus(), result.getCkanId()));
            } catch (Exception e) {
                log.error("ERROR. codelist metadata were not saved in externalCkanItem ckanId:" + (result.getCkanId() != null ? result.getCkanId() : WHITHOUT_CKAN_ID_ERROR), e);
            }
        }
    }

    @KafkaListener(id = KafkaProperties.GROUP_ID + "-conceptSchemesPublicationTopicId", groupId = KafkaProperties.GROUP_ID, topics = "#{kafkaProperties.getConceptSchemesPublicationTopic()}")
    public void processConceptSchemePublicationEvent(ConsumerRecord<String, ConceptSchemeAvro> consumerRecord) {
        ConceptSchemeAvro conceptScheme = consumerRecord.value();
        if (checkCanBePublished(conceptScheme.getProcStatus(), conceptScheme.getLatestVersionNumberPublic())) {
            PublicationConsumerProcessResult result = consumerProcessFacade.processConceptSchemePublicationMessage(conceptScheme);
            try {
                externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(conceptScheme, result.getStatus(), result.getCkanId()));
            } catch (Exception e) {
                log.error("ERROR. concept scheme metadata were not saved in externalCkanItem  ckanId:" + (result.getCkanId() != null ? result.getCkanId() : WHITHOUT_CKAN_ID_ERROR), e);
            }
        }
    }

    @KafkaListener(id = KafkaProperties.GROUP_ID + "-categorySchemesPublicationTopicId", groupId = KafkaProperties.GROUP_ID, topics = "#{kafkaProperties.getCategorySchemesPublicationTopic()}")
    public void processCategorySchemePublicationEvent(ConsumerRecord<String, CategorySchemeAvro> consumerRecord) {
        CategorySchemeAvro categoryScheme = consumerRecord.value();
        if (checkCanBePublished(categoryScheme.getProcStatus(), categoryScheme.getLatestVersionNumberPublic())) {
            PublicationConsumerProcessResult result = consumerProcessFacade.processCategorySchemePublicationMessage(categoryScheme);
            try {
                externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(categoryScheme, result.getStatus(), result.getCkanId()));
            } catch (Exception e) {
                log.error("ERROR. category scheme metadata were not saved in externalCkanItem ckanId:" + (result.getCkanId() != null ? result.getCkanId() : WHITHOUT_CKAN_ID_ERROR), e);
            }
        }
    }

    @KafkaListener(id = KafkaProperties.GROUP_ID + "-organisationSchemesPublicationTopicId", groupId = KafkaProperties.GROUP_ID, topics = "#{kafkaProperties.getOrganisationSchemesPublicationTopic()}")
    public void processOrganisationSchemePublicationEvent(ConsumerRecord<String, OrganisationSchemeAvro> consumerRecord) {
        OrganisationSchemeAvro organisationScheme = consumerRecord.value();
        if (checkCanBePublished(organisationScheme.getProcStatus(), organisationScheme.getLatestVersionNumberPublic())) {
            PublicationConsumerProcessResult result = consumerProcessFacade.processOrganisationSchemePublicationMessage(organisationScheme);
            try {
                externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(organisationScheme, result.getStatus(), result.getCkanId()));
            } catch (Exception e) {
                log.error("ERROR. organisation scheme metadata were not saved in externalCkanItem ckanId:" + (result.getCkanId() != null ? result.getCkanId() : WHITHOUT_CKAN_ID_ERROR), e);
            }
        }
    }

    private boolean checkCanBePublished(ProcStatusEnumAvro resourceStatus, Boolean isLatestNumberVersionExternallyPublished) {
        return ProcStatusEnumAvro.EXTERNALLY_PUBLISHED.equals(resourceStatus) && Boolean.TRUE.equals(isLatestNumberVersionExternallyPublished);
    }
}
