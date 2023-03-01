package es.gobcan.istac.edatos.publisher.service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackageResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResourceResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanSearchResponse;

public interface CkanService {

    CkanSearchResponse getDatasetByName(String name);
    CkanPackageResponse createDataset(CkanPackage dataset);
    CkanResourceResponse createResource(CkanResource ckanResource);
    CkanPackageResponse updateDataset(CkanPackage dataset);
    CkanResourceResponse updateResource(CkanResource resource);
}
