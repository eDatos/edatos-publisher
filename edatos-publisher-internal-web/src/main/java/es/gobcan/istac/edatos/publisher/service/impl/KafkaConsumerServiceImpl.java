package es.gobcan.istac.edatos.publisher.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanGenericResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackageResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanSearchResponse;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCkanItemService;
import es.gobcan.istac.edatos.publisher.core.service.NotificationService;
import es.gobcan.istac.edatos.publisher.mapper.CategoryAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.ExternalCkanItemMapper;
import es.gobcan.istac.edatos.publisher.service.CkanService;
import es.gobcan.istac.edatos.publisher.service.KafkaConsumerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.siemac.metamac.srm.core.stream.message.*;
import org.siemac.metamac.statistical.operations.core.stream.messages.OperationAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerServiceImpl.class);

    private final CkanService ckanService;
    private final NotificationService notificationService;
    private final CategoryAvro2CkanPackageMapper categoryAvro2CkanPackageMapper;
    private final ExternalCkanItemMapper externalCkanItemMapper;
    private final ExternalCkanItemService externalCkanItemService;

    public KafkaConsumerServiceImpl(CkanService ckanService, NotificationService notificationService, CategoryAvro2CkanPackageMapper categoryAvro2CkanPackageMapper, ExternalCkanItemService externalCkanItemService, ExternalCkanItemMapper externalCkanItemMapper) {
        this.ckanService = ckanService;
        this.notificationService = notificationService;
        this.categoryAvro2CkanPackageMapper = categoryAvro2CkanPackageMapper;
        this.externalCkanItemService = externalCkanItemService;
        this.externalCkanItemMapper = externalCkanItemMapper;
    }

    @KafkaListener(topics = "#{kafkaProperties.getDatasetPublicationTopic()}")
    public void processDatasetPublicationEvent(ConsumerRecord<String, DatasetVersionAvro> consumerRecord) {
        DatasetVersionAvro dataset = consumerRecord.value(); // TODO EDATOS-3460
    }

    @KafkaListener(topics = "#{kafkaProperties.getOperationPublicationTopic()}")
    public void processOperationPublicationEvent(ConsumerRecord<String, OperationAvro> consumerRecord) {
        OperationAvro operation = consumerRecord.value(); // TODO EDATOS-3460
    }

    @KafkaListener(topics = "#{kafkaProperties.getCodelistsPublicationTopic()}")
    public void processCodelistPublicationEvent(ConsumerRecord<String, CodelistAvro> consumerRecord) {
        CodelistAvro codelist = consumerRecord.value(); // TODO EDATOS-3460
    }

    @KafkaListener(topics = "#{kafkaProperties.getDsdPublicationTopic()}")
    public void processDsdPublicationEvent(ConsumerRecord<String, DataStructureDefinitionAvro> consumerRecord) {
        DataStructureDefinitionAvro dataStructureDefinition = consumerRecord.value(); // TODO EDATOS-3460
    }

    @KafkaListener(topics = "#{kafkaProperties.getConceptSchemesPublicationTopic()}")
    public void processConceptSchemePublicationEvent(ConsumerRecord<String, ConceptSchemeAvro> consumerRecord) {
        ConceptSchemeAvro conceptScheme = consumerRecord.value(); // TODO EDATOS-3460
    }

    @KafkaListener(topics = "#{kafkaProperties.getOrganisationSchemesPublicationTopic()}")
    public void processOrganisationSchemePublicationEvent(ConsumerRecord<String, OrganisationSchemeAvro> consumerRecord) {
        OrganisationSchemeAvro organisationScheme = consumerRecord.value(); // TODO EDATOS-3460
    }

    @KafkaListener(topics = "#{kafkaProperties.getCategorySchemesPublicationTopic()}")
    public void processCategorySchemePublicationEvent(ConsumerRecord<String, CategorySchemeAvro> consumerRecord) {
        CategorySchemeAvro categoryScheme = consumerRecord.value();
        createOrUpdateCkanPackage(categoryAvro2CkanPackageMapper.toCkanPackage(categoryScheme), "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=ISTAC:TEMAS_CANARIAS(01.003)");
        externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(categoryScheme));
    }

    void createOrUpdateCkanPackage(CkanPackage ckanPackage, String urn) {
        try {
            CkanSearchResponse existingDataset = ckanService.getDatasetByUrn(urn);

            if(existingDataset.isSuccess()) {
                CkanPackageResponse packageResponse;

                if (existingDataset.getResult().getCount() > 0) {
                    packageResponse = ckanService.createDataset(ckanPackage);
                    //packageResponse = ckanService.updateDataset(existingDataset.getResult().getResults().get(0).getId(), categoryScheme);  // TODO EDATOS-3460
                } else {
                    packageResponse = ckanService.createDataset(ckanPackage);
                }
                if(!packageResponse.isSuccess()) {
                    notificationService.createFailedDatasetPublicationNotification(urn, packageResponse.getError(), null);
                }
            } else {
                notificationService.createFailedDatasetPublicationNotification(urn, existingDataset.getError(), null);
            }
        } catch(HttpStatusCodeException e) {
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

        if(e.getStatusCode().is5xxServerError() || response.getHelp() == null) {
            throw e;
        }
    }
}
