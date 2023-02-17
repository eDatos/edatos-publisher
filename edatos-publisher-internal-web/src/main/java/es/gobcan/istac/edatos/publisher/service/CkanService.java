package es.gobcan.istac.edatos.publisher.service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.*;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;

public interface CkanService {

    CkanSearchResponse getDatasetByUrn(String urn);
    CkanPackageResponse createDataset(CkanPackage dataset);
    CkanResourceResponse createResource(String package_id, String url);
    CkanGenericResponse updateDataset(String id, CategorySchemeAvro category);
}
