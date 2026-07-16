package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;
import es.gobcan.istac.edatos.publisher.core.organisation.service.OrganisationSchemePublisherService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;

@Component
public class OrganisationSchemePublicationConsumerProcess extends PublicationConsumerProcess implements ConsumerProcess<OrganisationSchemeAvro> {

    @Autowired
    OrganisationSchemePublisherService organisationSchemeService;

    @Override
    public PublicationConsumerProcessResult processPublicationEvent(OrganisationSchemeAvro organisationScheme) {

        String urn = organisationScheme.getUrn();
        CkanPackage ckanPackage = null;
        String urnWithoutVersion = CkanFormatUtil.formatCkanNameFromDatasetUrn(urn);
        CkanPackage existingPackageInCkan = getCkanPackage(urnWithoutVersion);

        if (existingPackageInCkan == null) {
            return new PublicationConsumerProcessResult(false, null, CkanState.FAILED);
        }

        if (existingPackageInCkan.getId() != null) {
            ckanPackage = organisationSchemeService.retrieveUpdateCkanPackage(organisationScheme, existingPackageInCkan.getId());
        } else {
            ckanPackage = organisationSchemeService.retrieveNewCkanPackage(organisationScheme);
        }

        if (ckanPackage != null) {
            String ckanId = existingPackageInCkan.getId();
            CkanPackage packageResult = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, existingPackageInCkan.getId());
            if (packageResult != null) {
                if (existingPackageInCkan.getId() != null) {
                    createOrUpdateCkanResources(organisationScheme, existingPackageInCkan, existingPackageInCkan.getId(), urn);
                } else {
                    ckanId = packageResult.getId();
                    createPackageRelationshipEntity(urnWithoutVersion, ckanId);
                }
                return new PublicationConsumerProcessResult(true, ckanId, CkanState.SENT);
            }
        }
        return new PublicationConsumerProcessResult(false, null, CkanState.FAILED);
    }

    private void createOrUpdateCkanResources(OrganisationSchemeAvro organisationScheme, CkanPackage actualPackage, String packageName, String urn) {
        List<CkanResource> resources = organisationSchemeService.retrieveUpdatedResources(organisationScheme, actualPackage, packageName);
        ckanSenderService.updateResources(resources, urn);
        resources = organisationSchemeService.retrieveNewResources(organisationScheme, actualPackage);
        if (!resources.isEmpty()) {
            ckanSenderService.createResources(resources, urn);
        }
    }
}
