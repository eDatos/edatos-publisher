package es.gobcan.istac.edatos.publisher.core.dataset.service;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.mappers.DatasetAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class DatasetPublisherServiceImpl implements DatasetPublisherService {

    private final Logger log = LoggerFactory.getLogger(DatasetPublisherServiceImpl.class);

    @Autowired
    private MetadataConfigurationService metadataConfigurationService;

    @Autowired
    private ExternalCategoryService externalCategoryService;

    @Autowired
    ExternalOperationService externalOperationService;

    @Override
    public CkanPackage retrieveNewCkanPackage(DatasetVersionAvro datasetVersion) {
        try {
            return DatasetAvro2CkanPackageMapper.toNewCkanPackage(datasetVersion, metadataConfigurationService, externalCategoryService, externalOperationService);
        } catch (Exception e) {
            log.debug("Error in new dataset  version publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackage(DatasetVersionAvro datasetVersion, String id) {
        try {
            return DatasetAvro2CkanPackageMapper.toExistingCkanPackage(datasetVersion, metadataConfigurationService, externalCategoryService, externalOperationService, id);
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
    public List<CkanResource> retrieveUpdatedResources(DatasetVersionAvro datasetVersion, CkanPackage existingPackage, String packageId) {
        try {
            return DatasetAvro2CkanPackageMapper.updateResourcesFromDatasetVersion(datasetVersion, metadataConfigurationService, packageId, existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve updated resources: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveNewResources(DatasetVersionAvro datasetVersion, String packageId) {
        try {
            return DatasetAvro2CkanPackageMapper.createResourcesFromDatasetVersion(datasetVersion, metadataConfigurationService, packageId);
        } catch (Exception e) {
            log.debug("Error in retrieve new resources: {}", e);
        }
        return null;
    }
}
