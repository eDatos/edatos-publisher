package es.gobcan.istac.edatos.publisher.core.service.impl;

import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.dataset.mappers.ItemResourceMapper;
import es.gobcan.istac.edatos.publisher.core.service.EDatosApisLocator;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;

@Service
public class ExternalCategoryServiceImpl implements ExternalCategoryService {

    /**
     * Amount of resources that can be obtained in a request.
     */
    private static final String LIMIT = String.valueOf(Integer.MAX_VALUE);
    private static final Logger LOG = LoggerFactory.getLogger(ExternalCategoryServiceImpl.class);

    private final EDatosApisLocator eDatosApisLocator;

    public ExternalCategoryServiceImpl(EDatosApisLocator eDatosApisLocator, ItemResourceMapper itemResourceMapper) {
        this.eDatosApisLocator = eDatosApisLocator;
    }

    @Override
    public Category getCategory(Pageable pageable, String agency, String resourceId, String version, String categoryId) {
        LOG.info("Making request to Structural Resources Manager to GET a category. agency: {}, resourceId: {}, version: {}, categoryId: {}",
                agency + "/" + resourceId + "/" + version + "/" + categoryId);

        return eDatosApisLocator.srmExternal().retrieveCategory(agency, resourceId, version, categoryId);

    }

}
