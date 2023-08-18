package es.gobcan.istac.edatos.publisher.service.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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

import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCkanItemService;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.ExternalCkanItemMapper;
import es.gobcan.istac.edatos.publisher.service.KafkaConsumerService;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private final Logger                  log = LoggerFactory.getLogger(KafkaConsumerServiceImpl.class);

    private final ExternalCkanItemMapper  externalCkanItemMapper;
    private final ExternalCkanItemService externalCkanItemService;
    private final ConsumerProcessFacade   consumerProcessFacade;

    public KafkaConsumerServiceImpl(ConsumerProcessFacade consumerProcessFacade, ExternalCkanItemService externalCkanItemService, ExternalCkanItemMapper externalCkanItemMapper) {
        this.consumerProcessFacade = consumerProcessFacade;
        this.externalCkanItemService = externalCkanItemService;
        this.externalCkanItemMapper = externalCkanItemMapper;
    }

    @KafkaListener(topics = "#{kafkaProperties.getDatasetPublicationTopic()}")
    public void processDatasetPublicationEvent(ConsumerRecord<String, DatasetVersionAvro> consumerRecord) {
        DatasetVersionAvro datasetVersion = consumerRecord.value();
        boolean isProcessed = consumerProcessFacade.processDatasetPublicationMessage(datasetVersion);
        try {
            externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(datasetVersion, isProcessed ? CkanState.SENT : CkanState.FAILED));
        } catch (Exception e) {
            log.error("ERROR. dataset metadata were not saved in externalCkanItem ", e);
        }

    }

    @KafkaListener(topics = "#{kafkaProperties.getCodelistsPublicationTopic()}")
    public void processCodelistPublicationEvent(ConsumerRecord<String, CodelistAvro> consumerRecord) {
        CodelistAvro codelist = consumerRecord.value();
        if (checkCanBePublished(codelist.getProcStatus(), codelist.getLatestVersionNumberPublic())) {
            boolean isProcessed = consumerProcessFacade.processCodelistPublicationMessage(codelist);
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
        if (checkCanBePublished(conceptScheme.getProcStatus(), conceptScheme.getLatestVersionNumberPublic())) {
            boolean isProcessed = consumerProcessFacade.processConceptSchemePublicationMessage(conceptScheme);
            try {
                externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(conceptScheme, isProcessed ? CkanState.SENT : CkanState.FAILED));
            } catch (Exception e) {
                log.error("ERROR. concept scheme metadata were not saved in externalCkanItem ", e);
            }
        }
    }

    @KafkaListener(topics = "#{kafkaProperties.getCategorySchemesPublicationTopic()}")
    public void processCategorySchemePublicationEvent(ConsumerRecord<String, CategorySchemeAvro> consumerRecord) {
        CategorySchemeAvro categoryScheme = consumerRecord.value();
        if (checkCanBePublished(categoryScheme.getProcStatus(), categoryScheme.getLatestVersionNumberPublic())) {
            boolean isProcessed = consumerProcessFacade.processCategorySchemePublicationMessage(categoryScheme);
            try {
                externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(categoryScheme, isProcessed ? CkanState.SENT : CkanState.FAILED));
            } catch (Exception e) {
                log.error("ERROR. category scheme metadata were not saved in externalCkanItem ", e);
            }
        }
    }

    @KafkaListener(topics = "#{kafkaProperties.getOrganisationSchemesPublicationTopic()}")
    public void processOrganisationSchemePublicationEvent(ConsumerRecord<String, OrganisationSchemeAvro> consumerRecord) {
        OrganisationSchemeAvro organisationScheme = consumerRecord.value();
        if (checkCanBePublished(organisationScheme.getProcStatus(), organisationScheme.getLatestVersionNumberPublic())) {
            boolean isProcessed = consumerProcessFacade.processOrganisationSchemePublicationMessage(organisationScheme);
            try {
                externalCkanItemService.save(externalCkanItemMapper.toExternalCkanItemEntity(organisationScheme, isProcessed ? CkanState.SENT : CkanState.FAILED));
            } catch (Exception e) {
                log.error("ERROR. organisation scheme metadata were not saved in externalCkanItem ", e);
            }
        }
    }

    private boolean checkCanBePublished(ProcStatusEnumAvro resourceStatus, Boolean isLatestNumberVersionExternallyPublished) {
        return ProcStatusEnumAvro.EXTERNALLY_PUBLISHED.equals(resourceStatus) && Boolean.TRUE.equals(isLatestNumberVersionExternallyPublished);
    }

}
