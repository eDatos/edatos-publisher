package es.gobcan.istac.edatos.publisher.core.service;

import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.springframework.data.domain.Pageable;

public interface ExternalCategoryService {

    Category getCategory(Pageable pageable, String agency, String resourceId, String version, String fields);
}
