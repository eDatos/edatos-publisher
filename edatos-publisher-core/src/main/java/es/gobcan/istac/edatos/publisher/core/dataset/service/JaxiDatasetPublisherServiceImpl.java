package es.gobcan.istac.edatos.publisher.core.dataset.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.mappers.JaxiDatasetAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.ibestat.jaxi.stream.messages.DatasetAvro;

@Service
public class JaxiDatasetPublisherServiceImpl implements JaxiDatasetPublisherService {

    private final Logger                 log = LoggerFactory.getLogger(JaxiDatasetPublisherServiceImpl.class);

    private MetadataConfigurationService metadataConfigurationService;
    private ExternalCategoryService      externalCategoryService;
    private ExternalOperationService     externalOperationService;
    private MessageSource                messageSource;

    @Autowired
    public JaxiDatasetPublisherServiceImpl(MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService, MessageSource messageSource) {
        this.metadataConfigurationService = metadataConfigurationService;
        this.externalCategoryService = externalCategoryService;
        this.externalOperationService = externalOperationService;
        this.messageSource = messageSource;
    }

    @Override
    public CkanPackage retrieveNewCkanPackage(DatasetAvro datasetVersion) {
        try {
            JaxiDatasetAvro2CkanPackageMapper jaxiDatasetAvro2CkanPackageMapper = new JaxiDatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return jaxiDatasetAvro2CkanPackageMapper.toNewCkanPackage(externalCategoryService, externalOperationService);
        } catch (Exception e) {
            log.debug("Error in new jaxi dataset version publication into ckan: ", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackage(DatasetAvro datasetVersion, String id) {
        try {

            JaxiDatasetAvro2CkanPackageMapper jaxiDatasetAvro2CkanPackageMapper = new JaxiDatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return jaxiDatasetAvro2CkanPackageMapper.toExistingCkanPackage(externalCategoryService, externalOperationService, id);
        } catch (Exception e) {
            log.debug("Error in existing jaxi dataset  version publication into ckan: ", e);
        }
        return null;
    }

    @Override
    public String retrieveCkanPackageName(DatasetAvro datasetVersion) {
        try {

            JaxiDatasetAvro2CkanPackageMapper jaxiDatasetAvro2CkanPackageMapper = new JaxiDatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return jaxiDatasetAvro2CkanPackageMapper.getCkanPackageName();
        } catch (Exception e) {
            log.debug("Error in retrieve jaxi dataset name: ", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveUpdatedResources(DatasetAvro datasetVersion, CkanPackage existingPackage, String packageId) {
        try {
            JaxiDatasetAvro2CkanPackageMapper jaxiDatasetAvro2CkanPackageMapper = new JaxiDatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return jaxiDatasetAvro2CkanPackageMapper.updateResources(packageId, existingPackage.getResources());
        } catch (Exception e) {
            log.debug("Error in retrieve updated jaxi resources: ", e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<CkanResource> retrieveNewResources(DatasetAvro datasetVersion, CkanPackage existingPackage) {
        try {

            JaxiDatasetAvro2CkanPackageMapper jaxiDatasetAvro2CkanPackageMapper = new JaxiDatasetAvro2CkanPackageMapper(datasetVersion, metadataConfigurationService, messageSource);

            return jaxiDatasetAvro2CkanPackageMapper.createResources(existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve new jaxi resources: ", e);
        }
        return new ArrayList<>();
    }
}
