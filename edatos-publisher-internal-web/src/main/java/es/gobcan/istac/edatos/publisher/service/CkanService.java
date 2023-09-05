package es.gobcan.istac.edatos.publisher.service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackageResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResourceResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanSearchResponse;

public interface CkanService {

    CkanSearchResponse getDatasetById(String id);
    CkanSearchResponse getDatasetByName(String name);
    CkanPackageResponse createPackage(CkanPackage ckanPackage);
    CkanResourceResponse createResource(CkanResource ckanResource);
    CkanPackageResponse updatePackage(CkanPackage ckanPackage);
    CkanResourceResponse updateResource(CkanResource resource);
}
