package es.gobcan.istac.edatos.publisher.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.siemac.metamac.srm.core.stream.message.*;
import org.siemac.metamac.statistical.operations.core.stream.messages.OperationAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.springframework.kafka.annotation.KafkaListener;

public interface KafkaConsumerService {

    void processDatasetPublicationEvent(ConsumerRecord<String, DatasetVersionAvro> consumerRecord);
    void processOperationPublicationEvent(ConsumerRecord<String, OperationAvro> consumerRecord);
    void processCodelistPublicationEvent(ConsumerRecord<String, CodelistAvro> consumerRecord);
    void processDsdPublicationEvent(ConsumerRecord<String, DataStructureDefinitionAvro> consumerRecord);
    void processConceptSchemePublicationEvent(ConsumerRecord<String, ConceptSchemeAvro> consumerRecord);
    void processOrganisationSchemePublicationEvent(ConsumerRecord<String, OrganisationSchemeAvro> consumerRecord);
    void processCategorySchemePublicationEvent(ConsumerRecord<String, CategorySchemeAvro> consumerRecord);
}
