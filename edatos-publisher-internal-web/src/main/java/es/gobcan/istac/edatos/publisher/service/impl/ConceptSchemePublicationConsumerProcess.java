package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.ckan.service.impl.CkanSenderService;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.concept.service.ConceptSchemePublisherService;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;

@Component
public class ConceptSchemePublicationConsumerProcess implements ConsumerProcess<ConceptSchemeAvro> {

    @Autowired
    CkanSenderService ckanSenderService;
    @Autowired
    ConceptSchemePublisherService conceptSchemeService;

    @Override
    public boolean processPublicationEvent(ConceptSchemeAvro conceptScheme) {

        String urn = conceptScheme.getUrn();
        CkanPackage ckanPackage = null;
        String packageName = conceptSchemeService.retrieveCkanPackageName(conceptScheme);
        CkanPackage actualPackage = ckanSenderService.getCkanPackageByName(urn, packageName);

        if (actualPackage == null) {
            return false;
        }

        if (actualPackage.getId() != null) {
            ckanPackage = conceptSchemeService.retrieveUpdateCkanPackage(conceptScheme, actualPackage.getId());
        } else {
            ckanPackage = conceptSchemeService.retrieveNewCkanPackage(conceptScheme);
        }

        if (ckanPackage != null) {
            Boolean resultado = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, actualPackage.getId());
            if (Boolean.TRUE.equals(resultado)) {
                if (actualPackage.getId() != null) {
                    List<CkanResource> resources = conceptSchemeService.retrieveUpdatedResources(conceptScheme, actualPackage, packageName);
                    ckanSenderService.updateResources(resources, urn);
                }
                return true;
            }

        }
        return false;
    }
}
