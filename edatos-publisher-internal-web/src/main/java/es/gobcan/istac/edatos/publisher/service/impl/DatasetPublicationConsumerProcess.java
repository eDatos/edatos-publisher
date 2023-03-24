package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.ckan.service.impl.CkanSenderService;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.dataset.service.DatasetPublisherService;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;

@Component
public class DatasetPublicationConsumerProcess implements ConsumerProcess<DatasetVersionAvro> {

    @Autowired
    CkanSenderService ckanSenderService;
    @Autowired
    DatasetPublisherService datasetService;

    @Override
    public boolean processPublicationEvent(DatasetVersionAvro datasetVersion) {

        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn();
        CkanPackage ckanPackage = null;
        String packageName = datasetService.retrieveCkanPackageName(datasetVersion);
        CkanPackage actualPackage = ckanSenderService.getCkanPackageByName(urn, packageName);

        if (actualPackage == null) {
            return false;
        }

        if (actualPackage.getId() != null) {
            ckanPackage = datasetService.retrieveUpdateCkanPackage(datasetVersion, actualPackage.getId());
        } else {
            ckanPackage = datasetService.retrieveNewCkanPackage(datasetVersion);
        }

        if (ckanPackage != null) {
            Boolean resultado = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, actualPackage.getId());
            if (Boolean.TRUE.equals(resultado)) {
                if (actualPackage.getId() != null) {
                    List<CkanResource> resources = datasetService.retrieveUpdatedResources(datasetVersion, actualPackage, packageName);
                    ckanSenderService.updateResources(resources, urn);
                }
                return true;
            }

        }
        return false;
    }

}
