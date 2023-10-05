package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.core.category.service.CategorySchemePublisherService;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;

@Component
public class CategorySchemePublicationConsumerProcess extends PublicationConsumerProcess implements ConsumerProcess<CategorySchemeAvro> {

    @Autowired
    CategorySchemePublisherService categorySchemePublisherService;

    @Override
    public boolean processPublicationEvent(CategorySchemeAvro categoryScheme) {

        String urn = categoryScheme.getUrn();
        CkanPackage ckanPackage = null;
        String urnWithoutVersion = CkanFormatUtil.formatCkanNameFromDatasetUrn(urn);
        CkanPackage existingPackageInCkan = getCkanPackage(urnWithoutVersion);

        if (existingPackageInCkan.getId() != null) {
            ckanPackage = categorySchemePublisherService.retrieveUpdateCkanPackage(categoryScheme, existingPackageInCkan.getId());
        } else {
            ckanPackage = categorySchemePublisherService.retrieveNewCkanPackage(categoryScheme);
        }

        if (ckanPackage != null) {
            CkanPackage packageResult = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, existingPackageInCkan.getId());
            if (packageResult != null) {
                if (existingPackageInCkan.getId() != null) {
                    createOrUpdateCkanResources(categoryScheme, existingPackageInCkan, existingPackageInCkan.getId(), urn);
                } else {
                    createPackageRelationshipEntity(urnWithoutVersion, packageResult.getId());
                }
                return true;
            }
        }
        return false;
    }

    private void createOrUpdateCkanResources(CategorySchemeAvro categoryScheme, CkanPackage actualPackage, String packageName, String urn) {
        List<CkanResource> resources = categorySchemePublisherService.retrieveUpdatedResources(categoryScheme, actualPackage, packageName);
        ckanSenderService.updateResources(resources, urn);
        resources = categorySchemePublisherService.retrieveNewResources(categoryScheme, actualPackage);
        if (!resources.isEmpty()) {
            ckanSenderService.createResources(resources, urn);
        }
    }
}
