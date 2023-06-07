package es.gobcan.istac.edatos.publisher.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;

public interface KafkaConsumerService {

    void processDatasetPublicationEvent(ConsumerRecord<String, DatasetVersionAvro> consumerRecord);
    void processCodelistPublicationEvent(ConsumerRecord<String, CodelistAvro> consumerRecord);
    void processConceptSchemePublicationEvent(ConsumerRecord<String, ConceptSchemeAvro> consumerRecord);
    void processOrganisationSchemePublicationEvent(ConsumerRecord<String, OrganisationSchemeAvro> consumerRecord);
    void processCategorySchemePublicationEvent(ConsumerRecord<String, CategorySchemeAvro> consumerRecord);
}
