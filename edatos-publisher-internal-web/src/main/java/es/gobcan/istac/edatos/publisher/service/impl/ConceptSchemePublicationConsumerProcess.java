package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.concept.service.ConceptSchemePublisherService;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;

@Component
public class ConceptSchemePublicationConsumerProcess extends PublicationConsumerProcess implements ConsumerProcess<ConceptSchemeAvro> {

    @Autowired
    ConceptSchemePublisherService conceptSchemeService;

    @Override
    public PublicationConsumerProcessResult processPublicationEvent(ConceptSchemeAvro conceptScheme) {

        String urn = conceptScheme.getUrn();
        CkanPackage ckanPackage = null;
        String urnWithoutVersion = CkanFormatUtil.formatCkanNameFromDatasetUrn(urn);
        CkanPackage existingPackageInCkan = getCkanPackage(urnWithoutVersion);

        if (existingPackageInCkan.getId() != null) {
            ckanPackage = conceptSchemeService.retrieveUpdateCkanPackage(conceptScheme, existingPackageInCkan.getId());
        } else {
            ckanPackage = conceptSchemeService.retrieveNewCkanPackage(conceptScheme);
        }

        if (ckanPackage != null) {
            String ckanId = existingPackageInCkan.getId();
            CkanPackage packageResult = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, existingPackageInCkan.getId());
            if (packageResult != null) {
                if (existingPackageInCkan.getId() != null) {
                    createOrUpdateCkanResources(conceptScheme, existingPackageInCkan, existingPackageInCkan.getId(), urn);
                } else {
                    ckanId = packageResult.getId();
                    createPackageRelationshipEntity(urnWithoutVersion, ckanId);
                }
                return new PublicationConsumerProcessResult(true, ckanId, CkanState.SENT);
            }

        }
        return new PublicationConsumerProcessResult(false, null, CkanState.FAILED);
    }

    private void createOrUpdateCkanResources(ConceptSchemeAvro conceptScheme, CkanPackage actualPackage, String packageName, String urn) {
        List<CkanResource> resources = conceptSchemeService.retrieveUpdatedResources(conceptScheme, actualPackage, packageName);
        ckanSenderService.updateResources(resources, urn);
        resources = conceptSchemeService.retrieveNewResources(conceptScheme, actualPackage);
        if (!resources.isEmpty()) {
            ckanSenderService.createResources(resources, urn);
        }
    }
}
