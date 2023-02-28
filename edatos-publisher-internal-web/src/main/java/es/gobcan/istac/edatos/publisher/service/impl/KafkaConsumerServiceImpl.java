package es.gobcan.istac.edatos.publisher.service.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.DataStructureDefinitionAvro;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.siemac.metamac.statistical.operations.core.stream.messages.OperationAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.ckan.service.impl.CkanSenderService;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.dataset.service.DatasetService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCkanItemService;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.ExternalCkanItemMapper;
import es.gobcan.istac.edatos.publisher.service.KafkaConsumerService;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerServiceImpl.class);

    private final CkanSenderService datasetSenderService;
    private final ExternalCkanItemMapper externalCkanItemMapper;
    private final ExternalCkanItemService externalCkanItemService;
    private final DatasetService datasetService;

    public KafkaConsumerServiceImpl(CkanSenderService datasetSenderService, DatasetService datasetService, ExternalCkanItemService externalCkanItemService,
            ExternalCkanItemMapper externalCkanItemMapper) {
        this.datasetSenderService = datasetSenderService;
        this.datasetService = datasetService;
        this.externalCkanItemService = externalCkanItemService;
        this.externalCkanItemMapper = externalCkanItemMapper;
    }

    @KafkaListener(topics = "#{kafkaProperties.getDatasetPublicationTopic()}")
    public void processDatasetPublicationEvent(ConsumerRecord<String, DatasetVersionAvro> consumerRecord) {
        DatasetVersionAvro datasetVersion = consumerRecord.value();
        Boolean isProcessed = processDatasetPublicationEvent(datasetVersion);
        if (isProcessed) { // TODO EDATOS-3460 ver qué poner cuando es un update y no un create. Puede haber error clave primaria?
            externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(datasetVersion));
        }
    }

    // @KafkaListener(topics = "#{kafkaProperties.getOperationPublicationTopic()}")
    public void processOperationPublicationEvent(ConsumerRecord<String, OperationAvro> consumerRecord) {
        OperationAvro operation = consumerRecord.value(); // TODO EDATOS-3460
    }

    // @KafkaListener(topics = "#{kafkaProperties.getCodelistsPublicationTopic()}")
    public void processCodelistPublicationEvent(ConsumerRecord<String, CodelistAvro> consumerRecord) {
        CodelistAvro codelist = consumerRecord.value(); // TODO EDATOS-3460
    }

    // @KafkaListener(topics = "#{kafkaProperties.getDsdPublicationTopic()}")
    public void processDsdPublicationEvent(ConsumerRecord<String, DataStructureDefinitionAvro> consumerRecord) {
        DataStructureDefinitionAvro dataStructureDefinition = consumerRecord.value(); // TODO EDATOS-3460
    }

    // @KafkaListener(topics = "#{kafkaProperties.getConceptSchemesPublicationTopic()}")
    public void processConceptSchemePublicationEvent(ConsumerRecord<String, ConceptSchemeAvro> consumerRecord) {
        ConceptSchemeAvro conceptScheme = consumerRecord.value(); // TODO EDATOS-3460
    }

    // @KafkaListener(topics = "#{kafkaProperties.getOrganisationSchemesPublicationTopic()}")
    public void processOrganisationSchemePublicationEvent(ConsumerRecord<String, OrganisationSchemeAvro> consumerRecord) {
        OrganisationSchemeAvro organisationScheme = consumerRecord.value(); // TODO EDATOS-3460
    }

    public Boolean processDatasetPublicationEvent(DatasetVersionAvro datasetVersion) {

        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn();
        CkanPackage ckanPackage = null;
        CkanPackage actualPackage = datasetSenderService.getCkanPackageByName(urn, datasetService.retrieveCkanPackageName(datasetVersion));

        if (actualPackage == null) {
            return false;
        }

        if (actualPackage.getId() != null) {
            ckanPackage = datasetService.retrieveUpdateCkanPackageFromDatasetVersion(datasetVersion, actualPackage.getId());
        } else {
            ckanPackage = datasetService.retrieveNewCkanPackageFromDatasetVersion(datasetVersion);
        }

        if (ckanPackage != null) {
            return datasetSenderService.createOrUpdateCkanPackage(ckanPackage, urn, actualPackage.getId());
        }
        return false;
    }

    @KafkaListener(topics = "#{kafkaProperties.getCategorySchemesPublicationTopic()}")
    public void processCategorySchemePublicationEvent(ConsumerRecord<String, CategorySchemeAvro> consumerRecord) {
        CategorySchemeAvro categoryScheme = consumerRecord.value();
        DatasetVersionAvro datasetVersion = null;
        // createOrUpdateCkanPackage(categoryAvro2CkanPackageMapper.toCkanPackage(categoryScheme), "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=ISTAC:TEMAS_CANARIAS(01.003)");
        // externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(categoryScheme));
    }

}
