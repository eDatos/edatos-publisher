package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.dataset.service.DatasetPublisherService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;

@Component
public class DatasetPublicationConsumerProcess extends PublicationConsumerProcess implements ConsumerProcess<DatasetVersionAvro> {

    private final Logger    log = LoggerFactory.getLogger(DatasetPublicationConsumerProcess.class);
    @Autowired
    DatasetPublisherService datasetService;

    @Override
    public boolean processPublicationEvent(DatasetVersionAvro datasetVersion) {

        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn();
        CkanPackage ckanPackage = null;
        String urnWithoutVersion = CkanFormatUtil.formatCkanNameFromDatasetUrn(urn);
        CkanPackage existingPackageInCkan = getCkanPackage(urnWithoutVersion);

        if (existingPackageInCkan.getId() != null) {
            ckanPackage = datasetService.retrieveUpdateCkanPackage(datasetVersion, existingPackageInCkan.getId());

            log.debug("process dataset urn " + urn + " existing package id: " + existingPackageInCkan.getId());
        } else {
            ckanPackage = datasetService.retrieveNewCkanPackage(datasetVersion);
        }

        if (ckanPackage != null) {
            CkanPackage packageResult = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, existingPackageInCkan.getId());
            if (packageResult != null) {
                if (existingPackageInCkan.getId() != null) {
                    createOrUpdateCkanResources(datasetVersion, existingPackageInCkan, existingPackageInCkan.getId(), urn);
                } else {
                    createPackageRelationshipEntity(urnWithoutVersion, packageResult.getId());
                }
                return true;
            }

        }
        return false;
    }

    private void createOrUpdateCkanResources(DatasetVersionAvro datasetVersion, CkanPackage actualPackage, String packageName, String urn) {
        List<CkanResource> resources = datasetService.retrieveUpdatedResources(datasetVersion, actualPackage, packageName);
        ckanSenderService.updateResources(resources, urn);
        resources = datasetService.retrieveNewResources(datasetVersion, actualPackage);
        if (!resources.isEmpty()) {
            ckanSenderService.createResources(resources, urn);
        }
        List<String> resourcesIdToDelete = datasetService.retrieveResourcesToDelete(datasetVersion, actualPackage);
        if (!resourcesIdToDelete.isEmpty()) {
            ckanSenderService.deleteResourcesById(resourcesIdToDelete, urn);
        }
    }

}
