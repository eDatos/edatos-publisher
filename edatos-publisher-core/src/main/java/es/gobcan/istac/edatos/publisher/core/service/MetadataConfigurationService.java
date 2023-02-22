package es.gobcan.istac.edatos.publisher.core.service;

import org.siemac.edatos.core.common.conf.ConfigurationService;
import org.siemac.edatos.core.common.exception.EDatosException;

public interface MetadataConfigurationService extends ConfigurationService {

    /**
     * Retrieves ckan licence id
     */
    public String retrieveCkanLicenceId() throws EDatosException;
}
