package es.gobcan.istac.edatos.publisher.core.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Component("metadataProperties")
public class MetadataProperties {

    private final Logger                 log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetadataConfigurationService configurationService;

    private String                       defaultLanguage;
    private List<String>                 availableLanguages;

    @PostConstruct
    public void setValues() {
        try {
            availableLanguages = configurationService.retrieveLanguages();
            defaultLanguage = availableLanguages.get(0);
        } catch (Exception e) {
            log.error("Error getting the value of a metadata {}", e);
        }
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public List<String> getAvailableLanguages() {
        // The languages are retrieved each time it is needed so that multi-language fields can be updated
        return availableLanguages;
    }
}
