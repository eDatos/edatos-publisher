package es.gobcan.istac.edatos.publisher.core.dataset.service;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.mappers.DatasetAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class DatasetPublisherServiceImpl implements DatasetPublisherService {

    private final Logger                 log = LoggerFactory.getLogger(DatasetPublisherServiceImpl.class);

    private MetadataConfigurationService metadataConfigurationService;
    private ExternalCategoryService      externalCategoryService;
    private ExternalOperationService     externalOperationService;
    private MessageSource                messageSource;

    @Autowired
    public DatasetPublisherServiceImpl(MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService,
            MessageSource messageSource) {
        this.metadataConfigurationService = metadataConfigurationService;
        this.externalCategoryService = externalCategoryService;
        this.externalOperationService = externalOperationService;
        this.messageSource = messageSource;
    }

    @Override
    public CkanPackage retrieveNewCkanPackage(DatasetVersionAvro datasetVersion) {
        try {
            DatasetAvro2CkanPackageMapper datasetAvro2CkanPackageMapper = new DatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return datasetAvro2CkanPackageMapper.toNewCkanPackage(externalCategoryService, externalOperationService);
        } catch (Exception e) {
            log.debug("Error in new dataset  version publication into ckan: ", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackage(DatasetVersionAvro datasetVersion, String id) {
        try {

            DatasetAvro2CkanPackageMapper datasetAvro2CkanPackageMapper = new DatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return datasetAvro2CkanPackageMapper.toExistingCkanPackage(externalCategoryService, externalOperationService, id);
        } catch (Exception e) {
            log.debug("Error in existing dataset  version publication into ckan: ", e);
        }
        return null;
    }

    @Override
    public String retrieveCkanPackageName(DatasetVersionAvro datasetVersion) {
        try {

            DatasetAvro2CkanPackageMapper datasetAvro2CkanPackageMapper = new DatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return datasetAvro2CkanPackageMapper.getCkanPackageName();
        } catch (Exception e) {
            log.debug("Error in retrieve dataset name: ", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveUpdatedResources(DatasetVersionAvro datasetVersion, CkanPackage existingPackage, String packageId) {
        try {
            DatasetAvro2CkanPackageMapper datasetAvro2CkanPackageMapper = new DatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return datasetAvro2CkanPackageMapper.updateResources(packageId, existingPackage.getResources());
        } catch (Exception e) {
            log.debug("Error in retrieve updated resources: ", e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<CkanResource> retrieveNewResources(DatasetVersionAvro datasetVersion, CkanPackage existingPackage) {
        try {

            DatasetAvro2CkanPackageMapper datasetAvro2CkanPackageMapper = new DatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return datasetAvro2CkanPackageMapper.createResources(existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve new resources: ", e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> retrieveResourcesToDelete(DatasetVersionAvro datasetVersion, CkanPackage existingPackage) {
        try {

            DatasetAvro2CkanPackageMapper datasetAvro2CkanPackageMapper = new DatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return datasetAvro2CkanPackageMapper.deleteResources(existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve  resources that must be deleted: ", e);
        }
        return null;
    }
}
