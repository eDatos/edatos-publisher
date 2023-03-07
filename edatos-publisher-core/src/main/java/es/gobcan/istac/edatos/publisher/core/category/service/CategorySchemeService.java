package es.gobcan.istac.edatos.publisher.core.category.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;

public interface CategorySchemeService {

    CkanPackage retrieveNewCkanPackage(CategorySchemeAvro categoryScheme);
    CkanPackage retrieveUpdateCkanPackage(CategorySchemeAvro categoryScheme, String id);
    String retrieveCkanPackageName(CategorySchemeAvro categoryScheme);
    List<CkanResource> retrieveUpdatedResources(CategorySchemeAvro categoryScheme, CkanPackage existingPackage, String packageId);
    List<CkanResource> retrieveNewResources(CategorySchemeAvro categoryScheme, String packageId);

}
