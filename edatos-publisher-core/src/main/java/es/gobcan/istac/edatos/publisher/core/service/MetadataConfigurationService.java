package es.gobcan.istac.edatos.publisher.core.service;

import org.siemac.edatos.core.common.conf.ConfigurationService;
import org.siemac.edatos.core.common.exception.EDatosException;

public interface MetadataConfigurationService extends ConfigurationService {

    String retrieveCkanLicenceId() throws EDatosException;
    String retrieveCkanApiKey() throws EDatosException;
    String retrieveCkanUrlBase() throws EDatosException;
    String retrieveCkanPublisherUri() throws EDatosException;
    String retrieveCkanSpatialUri() throws EDatosException;
    String retrieveCkanEniUrlBase() throws EDatosException;
}
