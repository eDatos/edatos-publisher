package es.gobcan.istac.edatos.publisher.core.dataset.service;

import java.util.List;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.ibestat.jaxi.stream.messages.DatasetAvro;

public interface JaxiDatasetPublisherService {

    CkanPackage retrieveNewCkanPackage(DatasetAvro datasetVersion);
    CkanPackage retrieveUpdateCkanPackage(DatasetAvro datasetVersion, String id);
    String retrieveCkanPackageName(DatasetAvro datasetVersion);
    List<CkanResource> retrieveUpdatedResources(DatasetAvro datasetVersion, CkanPackage existingPackage, String packageId);
    List<CkanResource> retrieveNewResources(DatasetAvro datasetVersion, CkanPackage existingPackage);
}
