package es.gobcan.istac.edatos.publisher.config.properties;

import org.springframework.context.annotation.Configuration;

import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Configuration
public class KafkaProperties {

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
        return metadataConfigurationService.retrieveKafkaTopicExternalDatasetPublication();
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
