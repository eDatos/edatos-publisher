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
}
