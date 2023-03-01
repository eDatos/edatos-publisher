package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.DataStructureDefinitionAvro;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.siemac.metamac.statistical.operations.core.stream.messages.OperationAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.ckan.service.impl.CkanSenderService;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.dataset.service.DatasetService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCkanItemService;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.ExternalCkanItemMapper;
import es.gobcan.istac.edatos.publisher.service.KafkaConsumerService;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

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
        OperationAvro operation = consumerRecord.value(); // TODO EDATOS-3460 PENDIENTE
    }

    // @KafkaListener(topics = "#{kafkaProperties.getCodelistsPublicationTopic()}")
    public void processCodelistPublicationEvent(ConsumerRecord<String, CodelistAvro> consumerRecord) {
        CodelistAvro codelist = consumerRecord.value(); // TODO EDATOS-3460 PENDIENTE
    }

    // @KafkaListener(topics = "#{kafkaProperties.getDsdPublicationTopic()}")
    public void processDsdPublicationEvent(ConsumerRecord<String, DataStructureDefinitionAvro> consumerRecord) {
        DataStructureDefinitionAvro dataStructureDefinition = consumerRecord.value(); // TODO EDATOS-3460 PENDIENTE
    }

    // @KafkaListener(topics = "#{kafkaProperties.getConceptSchemesPublicationTopic()}")
    public void processConceptSchemePublicationEvent(ConsumerRecord<String, ConceptSchemeAvro> consumerRecord) {
        ConceptSchemeAvro conceptScheme = consumerRecord.value(); // TODO EDATOS-3460 PENDIENTE
    }

    // @KafkaListener(topics = "#{kafkaProperties.getOrganisationSchemesPublicationTopic()}")
    public void processOrganisationSchemePublicationEvent(ConsumerRecord<String, OrganisationSchemeAvro> consumerRecord) {
        OrganisationSchemeAvro organisationScheme = consumerRecord.value(); // TODO EDATOS-3460 PENDIENTE
    }

    // @KafkaListener(topics = "#{kafkaProperties.getCategorySchemesPublicationTopic()}")
    public void processCategorySchemePublicationEvent(ConsumerRecord<String, CategorySchemeAvro> consumerRecord) {
        CategorySchemeAvro categoryScheme = consumerRecord.value(); // TODO EDATOS-3460 PENDIENTE
    }

    public Boolean processDatasetPublicationEvent(DatasetVersionAvro datasetVersion) {

        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn();
        CkanPackage ckanPackage = null;
        String packageName = datasetService.retrieveCkanPackageName(datasetVersion);
        CkanPackage actualPackage = datasetSenderService.getCkanPackageByName(urn, packageName);

        if (actualPackage == null) {
            return false;
        }

        if (actualPackage.getId() != null) {
            ckanPackage = datasetService.retrieveUpdateCkanPackageFromDatasetVersion(datasetVersion, actualPackage.getId());
        } else {
            ckanPackage = datasetService.retrieveNewCkanPackageFromDatasetVersion(datasetVersion);
        }

        if (ckanPackage != null) {
            Boolean resultado = datasetSenderService.createOrUpdateCkanPackage(ckanPackage, urn, actualPackage.getId());
            if (Boolean.TRUE.equals(resultado)) {
                if (actualPackage.getId() != null) {
                    List<CkanResource> resources = datasetService.retrieveUpdatedResources(datasetVersion, actualPackage, packageName);
                    datasetSenderService.updateResources(resources, urn);
                }
                return resultado;
            }

        }
        return false;
    }

}
