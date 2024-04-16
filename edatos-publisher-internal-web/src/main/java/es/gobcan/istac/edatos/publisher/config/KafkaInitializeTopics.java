package es.gobcan.istac.edatos.publisher.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Component
public class KafkaInitializeTopics {

    private static final Logger              LOGGER             = LoggerFactory.getLogger(KafkaInitializeTopics.class);
    private static final int                 NUM_OF_PARTITIONS  = 1;
    private static final short               NUM_OF_REPLICATION = (short) 1;
    private static final int                 TIMEOUT            = 1000;
    private static final String              RETENTION_MS       = "retention.ms";
    private static final Map<String, String> TOPIC_DEFAULT_SETTINGS;

    static {
        TOPIC_DEFAULT_SETTINGS = new HashMap<>();
        TOPIC_DEFAULT_SETTINGS.put(RETENTION_MS, "-1");
    }

    private final MetadataConfigurationService metadataConfigurationService;

    public KafkaInitializeTopics(MetadataConfigurationService metadataConfigurationService) {
        this.metadataConfigurationService = metadataConfigurationService;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext ac = event.getApplicationContext();
        if (ac.getParent() == null) {
            try {
                LOGGER.info("Initializing Kafka topics...");
                propagateCreationOfTopics();
            } catch (Exception e) {
                LOGGER.error("Could not initialize Kafka topics", e);
            }
        }
    }

    public Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, metadataConfigurationService.retrieveKafkaBootStrapServers());
        return properties;
    }

    private void propagateCreationOfTopics() {
        Properties kafkaProperties = getKafkaProperties();
        List<NewTopic> topics = getTopics();
        CreateTopicsOptions topicsOptions = getTopicsOptions();
        LOGGER.info("Kafka Properties: {}, Topics: {}", kafkaProperties, topics);
        createTopics(kafkaProperties, topics, topicsOptions);
    }

    private List<NewTopic> getTopics() {
        List<NewTopic> topics = new ArrayList<>();
        topics.add(createTopic(metadataConfigurationService.retrieveKafkaTopicDatasetsPublication()));
        topics.add(createTopic(metadataConfigurationService.retrieveKafkaTopicExternalDatasetPublication()));
        return topics;
    }

    private NewTopic createTopic(String topic) {
        return new NewTopic(topic, NUM_OF_PARTITIONS, NUM_OF_REPLICATION).configs(TOPIC_DEFAULT_SETTINGS);
    }

    private CreateTopicsOptions getTopicsOptions() {
        return new CreateTopicsOptions().timeoutMs(TIMEOUT);
    }

    private void createTopics(Properties kafkaProperties, List<NewTopic> topics, CreateTopicsOptions topicsOptions) {
        try (AdminClient adminClient = AdminClient.create(kafkaProperties)) {
            adminClient.createTopics(topics, topicsOptions).all().get();
        } catch (KafkaException | ExecutionException e) {
            if (e.getCause() instanceof TopicExistsException) {
                LOGGER.debug("{}", e.getCause().getMessage());
            } else {
                throw new RuntimeException("Imposible to create/check Topic in Kafka", e);
            }
        } catch (InterruptedException e) {
            LOGGER.error("Thread interrupted, imposible to create/check Topic in Kafka", e);
            Thread.currentThread().interrupt(); // See SonarLint java:S2142
        }
    }

}
