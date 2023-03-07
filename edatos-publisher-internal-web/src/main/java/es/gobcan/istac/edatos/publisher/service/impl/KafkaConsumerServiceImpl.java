package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.DataStructureDefinitionAvro;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.ProcStatusEnumAvro;
import org.siemac.metamac.statistical.operations.core.stream.messages.OperationAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.ckan.service.impl.CkanSenderService;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.codelist.service.CodelistService;
import es.gobcan.istac.edatos.publisher.core.concept.service.ConceptSchemeService;
import es.gobcan.istac.edatos.publisher.core.dataset.service.DatasetService;
import es.gobcan.istac.edatos.publisher.core.dataset.service.DatasetServiceImpl;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCkanItemService;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.ExternalCkanItemMapper;
import es.gobcan.istac.edatos.publisher.service.KafkaConsumerService;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private final Logger log = LoggerFactory.getLogger(DatasetServiceImpl.class);

    private final CkanSenderService ckanSenderService;
    private final ExternalCkanItemMapper externalCkanItemMapper;
    private final ExternalCkanItemService externalCkanItemService;
    private final DatasetService datasetService;
    private final CodelistService codelistService;
    private final ConceptSchemeService conceptSchemeService;

    public KafkaConsumerServiceImpl(CkanSenderService ckanSenderService, DatasetService datasetService, CodelistService codelistService, ConceptSchemeService conceptSchemeService,
            ExternalCkanItemService externalCkanItemService, ExternalCkanItemMapper externalCkanItemMapper) {
        this.ckanSenderService = ckanSenderService;
        this.datasetService = datasetService;
        this.codelistService = codelistService;
        this.conceptSchemeService = conceptSchemeService;
        this.externalCkanItemService = externalCkanItemService;
        this.externalCkanItemMapper = externalCkanItemMapper;
    }

    @KafkaListener(topics = "#{kafkaProperties.getDatasetPublicationTopic()}")
    public void processDatasetPublicationEvent(ConsumerRecord<String, DatasetVersionAvro> consumerRecord) {
        DatasetVersionAvro datasetVersion = consumerRecord.value();
        boolean isProcessed = processDatasetPublicationEvent(datasetVersion);
        try {
            externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(datasetVersion, isProcessed ? CkanState.SENT : CkanState.FAILED));
        } catch (Exception e) {
            log.error("ERROR. dataset metadata were not saved in externalCkanItem ", e);
        }

    }

    @KafkaListener(topics = "#{kafkaProperties.getCodelistsPublicationTopic()}")
    public void processCodelistPublicationEvent(ConsumerRecord<String, CodelistAvro> consumerRecord) {
        CodelistAvro codelist = consumerRecord.value();
        if (ProcStatusEnumAvro.EXTERNALLY_PUBLISHED.equals(codelist.getProcStatus())) {
            boolean isProcessed = processCodelistPublicationEvent(codelist);
            try {
                externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(codelist, isProcessed ? CkanState.SENT : CkanState.FAILED));
            } catch (Exception e) {
                log.error("ERROR. codelist metadata were not saved in externalCkanItem ", e);
            }
        }
    }

    @KafkaListener(topics = "#{kafkaProperties.getConceptSchemesPublicationTopic()}")
    public void processConceptSchemePublicationEvent(ConsumerRecord<String, ConceptSchemeAvro> consumerRecord) {
        ConceptSchemeAvro conceptScheme = consumerRecord.value();
        if (ProcStatusEnumAvro.EXTERNALLY_PUBLISHED.equals(conceptScheme.getProcStatus())) {
            boolean isProcessed = processConceptPublicationEvent(conceptScheme);
            try {
                externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(conceptScheme, isProcessed ? CkanState.SENT : CkanState.FAILED));
            } catch (Exception e) {
                log.error("ERROR. codelist metadata were not saved in externalCkanItem ", e);
            }
        }
    }

    // @KafkaListener(topics = "#{kafkaProperties.getOperationPublicationTopic()}")
    public void processOperationPublicationEvent(ConsumerRecord<String, OperationAvro> consumerRecord) {
        OperationAvro operation = consumerRecord.value(); // TODO EDATOS-3460 PENDIENTE
    }

    // @KafkaListener(topics = "#{kafkaProperties.getDsdPublicationTopic()}")
    public void processDsdPublicationEvent(ConsumerRecord<String, DataStructureDefinitionAvro> consumerRecord) {
        DataStructureDefinitionAvro dataStructureDefinition = consumerRecord.value(); // TODO EDATOS-3460 PENDIENTE
    }

    // @KafkaListener(topics = "#{kafkaProperties.getOrganisationSchemesPublicationTopic()}")
    public void processOrganisationSchemePublicationEvent(ConsumerRecord<String, OrganisationSchemeAvro> consumerRecord) {
        OrganisationSchemeAvro organisationScheme = consumerRecord.value(); // TODO EDATOS-3460 PENDIENTE
    }

    // @KafkaListener(topics = "#{kafkaProperties.getCategorySchemesPublicationTopic()}")
    public void processCategorySchemePublicationEvent(ConsumerRecord<String, CategorySchemeAvro> consumerRecord) {
        CategorySchemeAvro categoryScheme = consumerRecord.value(); // TODO EDATOS-3460 PENDIENTE
    }

    public boolean processDatasetPublicationEvent(DatasetVersionAvro datasetVersion) {

        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn();
        CkanPackage ckanPackage = null;
        String packageName = datasetService.retrieveCkanPackageName(datasetVersion);
        CkanPackage actualPackage = ckanSenderService.getCkanPackageByName(urn, packageName);

        if (actualPackage == null) {
            return false;
        }

        if (actualPackage.getId() != null) {
            ckanPackage = datasetService.retrieveUpdateCkanPackage(datasetVersion, actualPackage.getId());
        } else {
            ckanPackage = datasetService.retrieveNewCkanPackage(datasetVersion);
        }

        if (ckanPackage != null) {
            Boolean resultado = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, actualPackage.getId());
            if (Boolean.TRUE.equals(resultado)) {
                if (actualPackage.getId() != null) {
                    List<CkanResource> resources = datasetService.retrieveUpdatedResources(datasetVersion, actualPackage, packageName);
                    ckanSenderService.updateResources(resources, urn);
                }
                return resultado;
            }

        }
        return false;
    }

    public boolean processCodelistPublicationEvent(CodelistAvro codelist) {

        String urn = codelist.getUrn();
        CkanPackage ckanPackage = null;
        String packageName = codelistService.retrieveCkanPackageName(codelist);
        CkanPackage actualPackage = ckanSenderService.getCkanPackageByName(urn, packageName);

        if (actualPackage == null) {
            return false;
        }

        if (actualPackage.getId() != null) {
            ckanPackage = codelistService.retrieveUpdateCkanPackage(codelist, actualPackage.getId());
        } else {
            ckanPackage = codelistService.retrieveNewCkanPackage(codelist);
        }

        if (ckanPackage != null) {
            Boolean resultado = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, actualPackage.getId());
            if (Boolean.TRUE.equals(resultado)) {
                if (actualPackage.getId() != null) {
                    List<CkanResource> resources = codelistService.retrieveUpdatedResources(codelist, actualPackage, packageName);
                    ckanSenderService.updateResources(resources, urn);
                }
                return resultado;
            }

        }
        return false;
    }

    public boolean processConceptPublicationEvent(ConceptSchemeAvro conceptScheme) {

        String urn = conceptScheme.getUrn();
        CkanPackage ckanPackage = null;
        String packageName = conceptSchemeService.retrieveCkanPackageName(conceptScheme);
        CkanPackage actualPackage = ckanSenderService.getCkanPackageByName(urn, packageName);

        if (actualPackage == null) {
            return false;
        }

        if (actualPackage.getId() != null) {
            ckanPackage = conceptSchemeService.retrieveUpdateCkanPackage(conceptScheme, actualPackage.getId());
        } else {
            ckanPackage = conceptSchemeService.retrieveNewCkanPackage(conceptScheme);
        }

        if (ckanPackage != null) {
            Boolean resultado = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, actualPackage.getId());
            if (Boolean.TRUE.equals(resultado)) {
                if (actualPackage.getId() != null) {
                    List<CkanResource> resources = conceptSchemeService.retrieveUpdatedResources(conceptScheme, actualPackage, packageName);
                    ckanSenderService.updateResources(resources, urn);
                }
                return resultado;
            }

        }
        return false;
    }
}
