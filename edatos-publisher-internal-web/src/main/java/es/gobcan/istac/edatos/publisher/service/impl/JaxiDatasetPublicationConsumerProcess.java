package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.dataset.service.JaxiDatasetPublisherService;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;
import es.ibestat.jaxi.stream.messages.DatasetAvro;
import es.ibestat.jaxi.stream.messages.ProcStatusEnumAvro;

@Component
public class JaxiDatasetPublicationConsumerProcess extends PublicationConsumerProcess implements ConsumerProcess<DatasetAvro> {

    private final Logger        log = LoggerFactory.getLogger(JaxiDatasetPublicationConsumerProcess.class);

    JaxiDatasetPublisherService jaxiDatasetService;

    @Autowired
    public JaxiDatasetPublicationConsumerProcess(JaxiDatasetPublisherService jaxiDatasetService) {
        this.jaxiDatasetService = jaxiDatasetService;
    }

    private boolean hasValidStatisticalOperation(DatasetAvro jaxiDatasetVersion) {
        if (jaxiDatasetVersion.getStatisticalOperation() == null || jaxiDatasetVersion.getStatisticalOperation().getCode() == null) {
            log.error("[edatos-publisher] Jaxi dataset with null statistical operation received from Kafka. Dataset URN: {}", jaxiDatasetVersion.getUrn());
            return false;
        }
        return true;
    }

    @Override
    public PublicationConsumerProcessResult processPublicationEvent(DatasetAvro jaxiDatasetVersion) {

        if (!hasValidStatisticalOperation(jaxiDatasetVersion)) {
            return new PublicationConsumerProcessResult(false, null, CkanState.FAILED);
        }

        String urn = jaxiDatasetVersion.getUrn();
        CkanPackage ckanPackage = null;
        String urnWithoutVersion = CkanFormatUtil.formatCkanNameFromDatasetUrn(urn);
        CkanPackage existingPackageInCkan = getCkanPackage(urnWithoutVersion);

        if (existingPackageInCkan == null) {
            return new PublicationConsumerProcessResult(false, null, CkanState.FAILED);
        }

        PublicationConsumerProcessResult result = processArchivedDatasets(jaxiDatasetVersion, existingPackageInCkan, urn, urnWithoutVersion);

        if (result != null) {
            return result;
        }

        if (existingPackageInCkan.getId() != null) {
            ckanPackage = jaxiDatasetService.retrieveUpdateCkanPackage(jaxiDatasetVersion, existingPackageInCkan.getId());

            if (log.isDebugEnabled()) {
                log.debug(String.format("process jaxi dataset urn  %s existing package id: %s", urn, existingPackageInCkan.getId()));
            }
        } else {
            ckanPackage = jaxiDatasetService.retrieveNewCkanPackage(jaxiDatasetVersion);
        }

        if (ckanPackage != null) {
            String ckanId = existingPackageInCkan.getId();
            CkanPackage packageResult = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, existingPackageInCkan.getId());
            if (packageResult != null) {
                if (existingPackageInCkan.getId() != null) {
                    createOrUpdateCkanResources(jaxiDatasetVersion, existingPackageInCkan, existingPackageInCkan.getId(), urn);
                } else {
                    ckanId = packageResult.getId();
                    createPackageRelationshipEntity(urnWithoutVersion, packageResult.getId());
                }
                return new PublicationConsumerProcessResult(true, ckanId, CkanState.SENT);
            }

        }
        return new PublicationConsumerProcessResult(false, null, CkanState.FAILED);
    }

    private PublicationConsumerProcessResult processArchivedDatasets(DatasetAvro jaxiDatasetVersion, CkanPackage existingPackageInCkan, String urn, String urnWithoutVersion) {
        if (ProcStatusEnumAvro.ARCHIVED.equals(jaxiDatasetVersion.getProcStatus())) {
            boolean isDeleted = false;
            String ckanId = null;
            if (existingPackageInCkan.getId() == null) {
                if (log.isInfoEnabled()) {
                    log.info(String.format("process for deletion jaxi dataset urn  %s but dataset does not exists. Because of this, dataset has not been deleted", urn));
                }
            } else {
                isDeleted = deleteCkanPackage(urn, urnWithoutVersion, existingPackageInCkan);
                ckanId = existingPackageInCkan.getId();

            }
            return new PublicationConsumerProcessResult(isDeleted, ckanId, isDeleted ? CkanState.SENT_UNPUBLISHING : CkanState.FAILED_UNPUBLISHING);
        }
        return null;
    }

    private boolean deleteCkanPackage(String urn, String urnFormatCkan, CkanPackage existingPackageInCkan) {

        boolean deleted = ckanSenderService.deleteCkanPackage(existingPackageInCkan, urn);
        if (deleted) {
            deletePackageRelationshipEntity(urnFormatCkan);
        }
        return deleted;
    }

    private void createOrUpdateCkanResources(DatasetAvro jaxiDatasetVersion, CkanPackage actualPackage, String packageName, String urn) {
        List<CkanResource> resources = jaxiDatasetService.retrieveUpdatedResources(jaxiDatasetVersion, actualPackage, packageName);
        ckanSenderService.updateResources(resources, urn);
        resources = jaxiDatasetService.retrieveNewResources(jaxiDatasetVersion, actualPackage);
        if (!resources.isEmpty()) {
            ckanSenderService.createResources(resources, urn);
        }
    }

}
