package es.gobcan.istac.edatos.publisher.core.dataset.service;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.dataset.mappers.DatasetAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class DatasetServiceImpl implements DatasetService {

    private final Logger log = LoggerFactory.getLogger(DatasetServiceImpl.class);

    @Autowired
    private MetadataConfigurationService metadataConfigurationService;

    @Override
    public CkanPackage retrieveNewCkanPackageFromDatasetVersion(DatasetVersionAvro datasetVersion) {
        try {
            return getNewCkanPackageFromDataset(datasetVersion);
        } catch (Exception e) {
            log.debug("Error in new dataset  version publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackageFromDatasetVersion(DatasetVersionAvro datasetVersion, String id) {
        try {
            return getExistingCkanPackageFromDataset(datasetVersion, id);
        } catch (Exception e) {
            log.debug("Error in existing dataset  version publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public String retrieveCkanPackageName(DatasetVersionAvro datasetVersion) {
        try {
            return DatasetAvro2CkanPackageMapper.getCkanPackageName(datasetVersion);
        } catch (Exception e) {
            log.debug("Error in retrieve dataset name: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveUpdatedResources(DatasetVersionAvro datasetVersion, CkanPackage existingDataset, String packageId) {
        try {
            return updateResourcesFromDatasetVersion(datasetVersion, existingDataset, packageId);
        } catch (Exception e) {
            log.debug("Error in retrieve dataset name: {}", e);
        }
        return null;
    }

    private CkanPackage getNewCkanPackageFromDataset(DatasetVersionAvro datasetVersion) {
        return DatasetAvro2CkanPackageMapper.toNewCkanPackage(datasetVersion, metadataConfigurationService);
    }

    private CkanPackage getExistingCkanPackageFromDataset(DatasetVersionAvro datasetVersion, String id) {
        return DatasetAvro2CkanPackageMapper.toExistingCkanPackage(datasetVersion, id);
    }

    private List<CkanResource> updateResourcesFromDatasetVersion(DatasetVersionAvro datasetVersion, CkanPackage existingDataset, String packageId) {
        return DatasetAvro2CkanPackageMapper.updateResourcesFromDatasetVersion(datasetVersion, packageId, existingDataset);
    }

}
