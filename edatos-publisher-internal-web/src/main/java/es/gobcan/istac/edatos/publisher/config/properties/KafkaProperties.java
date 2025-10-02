package es.gobcan.istac.edatos.publisher.config.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Configuration
public class KafkaProperties {

    private static final Logger                LOGGER    = LoggerFactory.getLogger(KafkaProperties.class);

    private static final String                GROUP_ID  = "edatos-publisher";

    private static final String                CLIENT_ID = "edatos-publisher-client";

    private final MetadataConfigurationService metadataConfigurationService;

    public KafkaProperties(MetadataConfigurationService metadataConfigurationService) {
        this.metadataConfigurationService = metadataConfigurationService;
    }

    public String getOperationPublicationTopic() {
        return metadataConfigurationService.retrieveKafkaTopicOperationsPublication();
    }

    public String getDatasetPublicationTopic() {
        return metadataConfigurationService.retrieveKafkaTopicDatasetsPublication();
    }

    public String getJaxiDatasetPublicationTopic() {
        try {
            // The topic can not be enabled in some environments.
            return metadataConfigurationService.retrieveKafkaTopicExternalDatasetPublication();
        } catch (Exception e) {
            LOGGER.info("getJaxiDatasetPublicationTopic not found. Check if must exists in common metadata");
        }
        return null;
    }

    public String getCodelistsPublicationTopic() {
        return metadataConfigurationService.retrieveKafkaTopicCodelistsPublication();
    }

    public String getDsdPublicationTopic() {
        return metadataConfigurationService.retrieveKafkaTopicDsdPublication();
    }

    public String getConceptSchemesPublicationTopic() {
        return metadataConfigurationService.retrieveKafkaTopicConceptSchemesPublication();
    }

    public String getOrganisationSchemesPublicationTopic() {
        return metadataConfigurationService.retrieveKafkaTopicOrganisationSchemesPublication();
    }

    public String getCategorySchemesPublicationTopic() {
        return metadataConfigurationService.retrieveKafkaTopicCategorySchemesPublication();
    }

    public String getBootStrapServers() {
        return metadataConfigurationService.retrieveKafkaBootStrapServers();
    }

    public String getSchemaRegistryUrl() {
        return metadataConfigurationService.retrieveKafkaSchemaRegistryUrl();
    }

    public String getGroupId() {
        return KafkaProperties.GROUP_ID;
    }

    public String getClientId() {
        return KafkaProperties.CLIENT_ID;
    }
}
