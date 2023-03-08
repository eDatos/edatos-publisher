package es.gobcan.istac.edatos.publisher.core.service;

import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;

public interface ExternalCategoryService {

    Category getCategory(String agency, String resourceId, String version, String categoryId);
    CategorySchemes getCategoryByQuery(String agency, String resourceId, String query);
}
