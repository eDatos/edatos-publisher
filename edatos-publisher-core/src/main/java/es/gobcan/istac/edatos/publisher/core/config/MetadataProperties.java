package es.gobcan.istac.edatos.publisher.core.config;

import java.util.List;

import javax.annotation.PostConstruct;

import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metadataProperties")
public class MetadataProperties {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetadataConfigurationService configurationService;

    private String metamacCasPrefix;
    private String metamacCasLogoutUrl;
    private List<String> availableLanguages;

    private String internalAppUrl;
    private String casService;

    @PostConstruct
    public void setValues() {
        try {
            metamacCasPrefix = normalizeUrl(configurationService.retrieveSecurityCasServerUrlPrefix());
            metamacCasLogoutUrl = normalizeUrl(configurationService.retrieveSecurityCasServiceLogoutUrl());
            availableLanguages = configurationService.retrieveLanguages();
            internalAppUrl = normalizeUrl(configurationService.retrieveExternalUsersInternalWebApplicationUrlBase());
            casService = internalAppUrl + "/login/cas";
        } catch (Exception e) {
            log.error("Error getting the value of a metadata {}", e);
        }
    }

    public String getMetamacCasPrefix() {
        return metamacCasPrefix;
    }

    public String getMetamacCasLogoutUrl() {
        return metamacCasLogoutUrl;
    }

    public String getInternalAppUrl() {
        return internalAppUrl;
    }

    public String getCasService() {
        return casService;
    }

    public List<String> getAvailableLanguages() {
        // The languages are retrieved each time it is needed so that multi-language fields can be updated
        return availableLanguages;
    }

    private String normalizeUrl(String url) {
        return StringUtils.removeEnd(url, "/");
    }
}
