package es.gobcan.istac.edatos.publisher.core.dataset.service;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.dataset.mappers.DatasetAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class DatasetServiceImpl implements DatasetService {

    private final Logger log = LoggerFactory.getLogger(DatasetServiceImpl.class);

    @Autowired
    private MetadataConfigurationService metadataConfigurationService;

    @Override
    public CkanPackage retrieveCkanPackageFromDatasetVersion(DatasetVersionAvro datasetVersion) {
        try {
            return getCkanPackageFromDataset(datasetVersion);
        } catch (Exception e) {
            log.debug("Error in dataset  version publication into ckan: {}", e);
        }
        return null;
    }

    private CkanPackage getCkanPackageFromDataset(DatasetVersionAvro datasetVersion) {
        return DatasetAvro2CkanPackageMapper.toCkanPackage(datasetVersion, metadataConfigurationService);

    }

}
