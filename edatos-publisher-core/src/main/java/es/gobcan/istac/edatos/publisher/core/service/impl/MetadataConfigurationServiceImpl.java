package es.gobcan.istac.edatos.publisher.core.service.impl;

import org.siemac.edatos.core.common.conf.ConfigurationServiceImpl;
import org.siemac.edatos.core.common.exception.EDatosException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.config.DataConfigurationConstants;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class MetadataConfigurationServiceImpl extends ConfigurationServiceImpl implements MetadataConfigurationService {

    @Override
    public String retrieveCkanLicenceId() throws EDatosException {
        return retrieveProperty(DataConfigurationConstants.CKAN_LICENCE_ID);
    }

    @Override
    public String retrieveCkanApiKey() throws EDatosException {
        return retrieveProperty(DataConfigurationConstants.CKAN_API_KEY);
    }

    @Override
    public String retrieveCkanUrlBase() throws EDatosException {
        return retrieveProperty(DataConfigurationConstants.CKAN_URL_BASE);
    }

    @Override
    public String retrieveCkanPublisherUri() throws EDatosException {
        return retrieveProperty(DataConfigurationConstants.CKAN_PUBLISHER_URI);
    }

    @Override
    public String retrieveCkanSpatialUri() throws EDatosException {
        return retrieveProperty(DataConfigurationConstants.CKAN_SPATIAL_URI);
    }

    @Override
    public String retrieveCkanEniUrlBase() throws EDatosException {
        return retrieveProperty(DataConfigurationConstants.CKAN_ENI_URL_BASE);
    }

    @Override
    public String retrieveCkanDefaultUrnCategory() throws EDatosException {
        return retrieveProperty(DataConfigurationConstants.CKAN_DEFAULT_URN_CATEGORY);
    }

    @Override
    public String retrieveCkanDefaultOrganisation() throws EDatosException {
        return retrieveProperty(DataConfigurationConstants.CKAN_DEFAULT_ORGANISATION);
    }
}
