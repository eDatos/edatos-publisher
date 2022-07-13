package es.gobcan.istac.edatos.publisher.core.service;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.edatos.core.common.exception.EDatosException;
import org.siemac.metamac.notices.rest.internal.v1_0.service.NoticesV1_0;
import org.springframework.stereotype.Service;

@Service
public class EDatosApisLocator {

    private final MetadataConfigurationService metadataConfigurationService;

    private NoticesV1_0 noticesV10;
    public EDatosApisLocator(MetadataConfigurationService metadataConfigurationService) {
        this.metadataConfigurationService = metadataConfigurationService;
    }

    public NoticesV1_0 getNoticesRestInternalFacadeV10() throws EDatosException {
        if (noticesV10 == null) {
            String baseApi = metadataConfigurationService.retrieveNoticesInternalApiUrlBase();
            noticesV10 = JAXRSClientFactory.create(baseApi, NoticesV1_0.class, null, true);
        }
        WebClient.client(noticesV10).reset();
        WebClient.client(noticesV10).accept("application/xml");

        return noticesV10;
    }
}
