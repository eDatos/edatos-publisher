package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.ckan.service.impl.CkanSenderService;
import es.gobcan.istac.edatos.publisher.core.category.service.CategorySchemeService;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;

@Component
public class CategorySchemePublicationConsumerProcess implements ConsumerProcess<CategorySchemeAvro> {

    @Autowired
    CkanSenderService ckanSenderService;
    @Autowired
    CategorySchemeService categorySchemeService;

    @Override
    public boolean processPublicationEvent(CategorySchemeAvro categoryScheme) {

        String urn = categoryScheme.getUrn();
        CkanPackage ckanPackage = null;
        String packageName = categorySchemeService.retrieveCkanPackageName(categoryScheme);
        CkanPackage actualPackage = ckanSenderService.getCkanPackageByName(urn, packageName);

        if (actualPackage == null) {
            return false;
        }

        if (actualPackage.getId() != null) {
            ckanPackage = categorySchemeService.retrieveUpdateCkanPackage(categoryScheme, actualPackage.getId());
        } else {
            ckanPackage = categorySchemeService.retrieveNewCkanPackage(categoryScheme);
        }

        if (ckanPackage != null) {
            Boolean resultado = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, actualPackage.getId());
            if (Boolean.TRUE.equals(resultado)) {
                if (actualPackage.getId() != null) {
                    List<CkanResource> resources = categorySchemeService.retrieveUpdatedResources(categoryScheme, actualPackage, packageName);
                    ckanSenderService.updateResources(resources, urn);
                }
                return resultado;
            }
        }
        return false;
    }
}
