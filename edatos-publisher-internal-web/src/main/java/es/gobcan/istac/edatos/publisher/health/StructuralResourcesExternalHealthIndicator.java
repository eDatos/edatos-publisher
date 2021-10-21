package es.gobcan.istac.edatos.publisher.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

import static es.gobcan.istac.edatos.publisher.core.config.Constants.HEALTH_INDICATOR_REQUEST_TIMEOUT_MS;

@Component
public class StructuralResourcesExternalHealthIndicator extends AbstractHealthIndicator {
    private final MetadataConfigurationService metadataConfigurationService;

    public StructuralResourcesExternalHealthIndicator(MetadataConfigurationService metadataConfigurationService) {
        this.metadataConfigurationService = metadataConfigurationService;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        String endpoint = metadataConfigurationService.retrieveSrmExternalApiUrlBase() + "?_wadl";
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(HEALTH_INDICATOR_REQUEST_TIMEOUT_MS);
        factory.setReadTimeout(HEALTH_INDICATOR_REQUEST_TIMEOUT_MS);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            restTemplate.getForObject(endpoint, String.class);
            builder.withDetail("endpoint", endpoint).up();
        } catch (Exception e) {
            builder.withException(e).down();
        }
    }
}
