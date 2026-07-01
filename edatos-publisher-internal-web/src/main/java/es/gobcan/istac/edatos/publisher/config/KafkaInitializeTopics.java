package es.gobcan.istac.edatos.publisher.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.config.properties.KafkaProperties;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Component
public class KafkaInitializeTopics {

    private static final Logger              LOGGER             = LoggerFactory.getLogger(KafkaInitializeTopics.class);
    private static final int                 NUM_OF_PARTITIONS  = 1;
    private static final short               NUM_OF_REPLICATION = (short) 1;
    private static final String              RETENTION_MS       = "retention.ms";
    private static final Map<String, String> TOPIC_DEFAULT_SETTINGS;

    static {
        TOPIC_DEFAULT_SETTINGS = new HashMap<>();
        TOPIC_DEFAULT_SETTINGS.put(RETENTION_MS, "-1");
    }

    private final MetadataConfigurationService  metadataConfigurationService;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public KafkaInitializeTopics(MetadataConfigurationService metadataConfigurationService, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        this.metadataConfigurationService = metadataConfigurationService;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext ac = event.getApplicationContext();
        if (ac.getParent() == null) {
            try {
                LOGGER.info("Initializing Kafka topics...");
                startAvailableConsumers();
            } catch (Exception e) {
                LOGGER.error("Could not initialize Kafka topics", e);
            }
        }
    }

    private void startAvailableConsumers() {
        // statistical resources consumers
        runConsumer(KafkaProperties.GROUP_ID + "-datasetPublicationTopicId");
        NewTopic externalPublicationTopic = getExternalDatasetsPublicationTopic();
        if (externalPublicationTopic != null) {
            runConsumer(KafkaProperties.GROUP_ID + "-externalPublicationTopicId");
        }

        // srm consumers
        runConsumer(KafkaProperties.GROUP_ID + "-codelistsPublicationTopicId");
        runConsumer(KafkaProperties.GROUP_ID + "-conceptSchemesPublicationTopicId");
        runConsumer(KafkaProperties.GROUP_ID + "-categorySchemesPublicationTopicId");
        runConsumer(KafkaProperties.GROUP_ID + "-organisationSchemesPublicationTopicId");

    }

    private void runConsumer(String consumerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (listenerContainer != null && !listenerContainer.isRunning()) {
            listenerContainer.start();
        }
    }

    public Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, metadataConfigurationService.retrieveKafkaBootStrapServers());
        return properties;
    }

    private NewTopic getExternalDatasetsPublicationTopic() {
        try {
            String topicNameDatasetExternalPublication = metadataConfigurationService.retrieveKafkaTopicExternalDatasetPublication();
            return createTopic(topicNameDatasetExternalPublication);
        } catch (Exception e) {
            LOGGER.error("It is not possible create external publication topic");
        }
        return null;
    }

    private NewTopic createTopic(String topic) {
        return new NewTopic(topic, NUM_OF_PARTITIONS, NUM_OF_REPLICATION).configs(TOPIC_DEFAULT_SETTINGS);
    }

}
