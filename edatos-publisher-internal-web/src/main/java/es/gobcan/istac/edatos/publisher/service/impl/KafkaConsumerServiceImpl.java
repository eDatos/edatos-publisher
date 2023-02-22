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
import org.springframework.web.client.HttpStatusCodeException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanGenericResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackageResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanSearchResponse;
import es.gobcan.istac.edatos.publisher.core.dataset.service.DatasetService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCkanItemService;
import es.gobcan.istac.edatos.publisher.core.service.NotificationService;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.ExternalCkanItemMapper;
import es.gobcan.istac.edatos.publisher.service.CkanService;
import es.gobcan.istac.edatos.publisher.service.KafkaConsumerService;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerServiceImpl.class);

    private final CkanService ckanService;
    private final NotificationService notificationService;
    private final ExternalCkanItemMapper externalCkanItemMapper;
    private final ExternalCkanItemService externalCkanItemService;
    private final DatasetService datasetService;

    public KafkaConsumerServiceImpl(CkanService ckanService, NotificationService notificationService, DatasetService datasetService, ExternalCkanItemService externalCkanItemService,
            ExternalCkanItemMapper externalCkanItemMapper) {
        this.ckanService = ckanService;
        this.notificationService = notificationService;
        this.datasetService = datasetService;
        this.externalCkanItemService = externalCkanItemService;
        this.externalCkanItemMapper = externalCkanItemMapper;
    }

    @KafkaListener(topics = "#{kafkaProperties.getDatasetPublicationTopic()}")
    public void processDatasetPublicationEvent(ConsumerRecord<String, DatasetVersionAvro> consumerRecord) {
        DatasetVersionAvro datasetVersion = consumerRecord.value();

        processDatasetPublicationEvent(datasetVersion);
        // createOrUpdateCkanPackage(DatasetAvro2CkanPackageMapper.toCkanPackage(datasetVersion, metadataConfigurationService), datasetVersion.getSiemacMetadataStatisticalResource()
        // .getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn());
        // externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(datasetVersion));
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

    public void processDatasetPublicationEvent(DatasetVersionAvro datasetVersion) {
        String urn = "urn:siemac:org.siemac.metamac.infomodel.statisticalresources.dataset=istac:c00010A_000026(2.30)";
        CkanPackage ckanPackage = datasetService.retrieveCkanPackageFromDatasetVersion(datasetVersion);
        if (ckanPackage != null) {
            createOrUpdateCkanPackage(ckanPackage, urn);
            urn = "";
        }
    }

    // @KafkaListener(topics = "#{kafkaProperties.getCategorySchemesPublicationTopic()}")
    public void processCategorySchemePublicationEvent(ConsumerRecord<String, CategorySchemeAvro> consumerRecord) {
        CategorySchemeAvro categoryScheme = consumerRecord.value();
        DatasetVersionAvro datasetVersion = null;
        processDatasetPublicationEvent(datasetVersion);
        // createOrUpdateCkanPackage(categoryAvro2CkanPackageMapper.toCkanPackage(categoryScheme), "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=ISTAC:TEMAS_CANARIAS(01.003)");
        // externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(categoryScheme));
    }

    void createOrUpdateCkanPackage(CkanPackage ckanPackage, String urn) {

        try {
            ckanService.createResource("license_id_prueba", " http://www.gobiernodecanarias.org/istac/jaxi-istac/tabla.do?accion=jsonMtd&uuidConsulta=01316d91-8cc1-41dd-ac96-86ab033c71fca"); // TODO
                                                                                                                                                                                               // EDATOS-3460

        } catch (Exception e) {
            LOGGER.error("create resource", e);
        }

        try {
            CkanSearchResponse existingDataset = ckanService.getDatasetByUrn(urn);

            if (existingDataset.isSuccess()) {
                CkanPackageResponse packageResponse;

                if (existingDataset.getResult().getCount() > 0) {
                    packageResponse = ckanService.createDataset(ckanPackage);
                    // packageResponse = ckanService.updateDataset(existingDataset.getResult().getResults().get(0).getId(), categoryScheme); // TODO EDATOS-3460
                } else {
                    packageResponse = ckanService.createDataset(ckanPackage);
                }
                if (!packageResponse.isSuccess()) {
                    notificationService.createFailedDatasetPublicationNotification(urn, packageResponse.getError(), null);
                }
            } else {
                notificationService.createFailedDatasetPublicationNotification(urn, existingDataset.getError(), null);
            }
        } catch (HttpStatusCodeException e) {
            manageCkanPublicationError(e, urn);
        }
    }

    void manageCkanPublicationError(HttpStatusCodeException e, String urn) {
        CkanGenericResponse response;
        try {
            response = new ObjectMapper().readValue(e.getResponseBodyAsString(), CkanGenericResponse.class);
        } catch (JsonProcessingException ex) {
            response = new CkanGenericResponse();
        }

        notificationService.createFailedDatasetPublicationNotification(urn, response.getError(), e);

        if (e.getStatusCode().is5xxServerError() || response.getHelp() == null) {
            throw e;
        }
    }
}
