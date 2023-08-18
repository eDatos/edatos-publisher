package es.gobcan.istac.edatos.publisher.core.service.impl;

import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.service.EDatosApisLocator;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;

@Service
public class ExternalCategoryServiceImpl implements ExternalCategoryService {

    /**
     * Amount of resources that can be obtained in a request.
     */
    private static final Logger     LOG = LoggerFactory.getLogger(ExternalCategoryServiceImpl.class);

    private final EDatosApisLocator eDatosApisLocator;

    public ExternalCategoryServiceImpl(EDatosApisLocator eDatosApisLocator) {
        this.eDatosApisLocator = eDatosApisLocator;
    }

    @Override
    public Category getCategory(String agency, String resourceId, String version, String categoryId) {
        LOG.debug("Making request to Structural Resources Manager to GET a category. agency: {}, resourceId: {}, version: {}, categoryId: {}",
                agency + "/" + resourceId + "/" + version + "/" + categoryId);

        return eDatosApisLocator.srmExternal().retrieveCategory(agency, resourceId, version, categoryId, null);

    }

    @Override
    public CategorySchemes getCategoryByQuery(String agency, String resourceId, String query) {
        LOG.debug("Making request to Structural Resources Manager to GET a category. agency: {}, resourceId: {}, query: {}", agency + "/" + resourceId + "/" + query);

        return eDatosApisLocator.srmExternal().findCategorySchemes(agency, resourceId, query, null, null, null);

    }

}
