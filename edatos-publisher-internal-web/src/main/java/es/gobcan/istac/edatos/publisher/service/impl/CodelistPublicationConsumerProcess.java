package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.codelist.service.CodelistPublisherService;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;

@Component
public class CodelistPublicationConsumerProcess extends PublicationConsumerProcess implements ConsumerProcess<CodelistAvro> {

    @Autowired
    CodelistPublisherService codelistService;

    @Override
    public PublicationConsumerProcessResult processPublicationEvent(CodelistAvro codelist) {

        String urn = codelist.getUrn();
        CkanPackage ckanPackage = null;
        String urnWithoutVersion = CkanFormatUtil.formatCkanNameFromDatasetUrn(urn);
        CkanPackage existingPackageInCkan = getCkanPackage(urnWithoutVersion);

        if (existingPackageInCkan.getId() != null) {
            ckanPackage = codelistService.retrieveUpdateCkanPackage(codelist, existingPackageInCkan.getId());
        } else {
            ckanPackage = codelistService.retrieveNewCkanPackage(codelist);
        }

        if (ckanPackage != null) {
            String ckanId = existingPackageInCkan.getId();
            CkanPackage packageResult = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, existingPackageInCkan.getId());
            if (packageResult != null) {
                if (existingPackageInCkan.getId() != null) {
                    createOrUpdateCkanResources(codelist, existingPackageInCkan, existingPackageInCkan.getId(), urn);
                } else {
                    ckanId = packageResult.getId();
                    createPackageRelationshipEntity(urnWithoutVersion, ckanId);
                }
                return new PublicationConsumerProcessResult(true, ckanId, CkanState.SENT);
            }

        }
        return new PublicationConsumerProcessResult(false, null, CkanState.FAILED);
    }

    private void createOrUpdateCkanResources(CodelistAvro codelist, CkanPackage actualPackage, String packageName, String urn) {
        List<CkanResource> resources = codelistService.retrieveUpdatedResources(codelist, actualPackage, packageName);
        ckanSenderService.updateResources(resources, urn);
        resources = codelistService.retrieveNewResources(codelist, actualPackage);
        if (!resources.isEmpty()) {
            ckanSenderService.createResources(resources, urn);
        }
    }
}
