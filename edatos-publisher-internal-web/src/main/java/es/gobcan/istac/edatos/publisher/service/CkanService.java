package es.gobcan.istac.edatos.publisher.service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanGenericResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackageResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResourceResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanSearchResponse;

public interface CkanService {

    CkanSearchResponse getDatasetByUrn(String urn);
    CkanPackageResponse createDataset(CkanPackage dataset);
    CkanResourceResponse createResource(CkanResource ckanResource);
    CkanGenericResponse updateDataset(String datasetId, CkanPackage dataset);
}
