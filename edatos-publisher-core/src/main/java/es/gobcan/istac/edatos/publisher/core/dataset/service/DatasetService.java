package es.gobcan.istac.edatos.publisher.core.dataset.service;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;

public interface DatasetService {

    CkanPackage retrieveNewCkanPackageFromDatasetVersion(DatasetVersionAvro datasetVersion);
    CkanPackage retrieveUpdateCkanPackageFromDatasetVersion(DatasetVersionAvro datasetVersion, String id);
    String retrieveCkanPackageName(DatasetVersionAvro datasetVersion);

}
