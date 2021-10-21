package es.gobcan.istac.edatos.publisher.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.siemac.metamac.statistical.operations.core.stream.messages.OperationAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;

public interface KafkaConsumerService {

    void processDatasetPublicationEvent(ConsumerRecord<String, DatasetVersionAvro> consumerRecord);
    void processOperationPublicationEvent(ConsumerRecord<String, OperationAvro> consumerRecord);
}
