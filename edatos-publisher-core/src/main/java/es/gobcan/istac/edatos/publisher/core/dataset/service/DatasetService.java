package es.gobcan.istac.edatos.publisher.core.dataset.service;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;

public interface DatasetService {

    CkanPackage retrieveNewCkanPackageFromDatasetVersion(DatasetVersionAvro datasetVersion);
    CkanPackage retrieveUpdateCkanPackageFromDatasetVersion(DatasetVersionAvro datasetVersion, String id);
    String retrieveCkanPackageName(DatasetVersionAvro datasetVersion);
    List<CkanResource> retrieveUpdatedResources(DatasetVersionAvro datasetVersion, CkanPackage existingDataset, String packageId);
    List<CkanResource> retrieveNewResources(DatasetVersionAvro datasetVersion, String packageId);

}
