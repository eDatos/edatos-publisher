package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.ckan.service.impl.CkanSenderService;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.organisation.service.OrganisationSchemeService;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;

@Component
public class OrganisationSchemePublicationConsumerProcess implements ConsumerProcess<OrganisationSchemeAvro> {

    @Autowired
    CkanSenderService ckanSenderService;
    @Autowired
    OrganisationSchemeService organisationSchemeService;

    @Override
    public boolean processPublicationEvent(OrganisationSchemeAvro organisationScheme) {

        String urn = organisationScheme.getUrn();
        CkanPackage ckanPackage = null;
        String packageName = organisationSchemeService.retrieveCkanPackageName(organisationScheme);
        CkanPackage actualPackage = ckanSenderService.getCkanPackageByName(urn, packageName);

        if (actualPackage == null) {
            return false;
        }

        if (actualPackage.getId() != null) {
            ckanPackage = organisationSchemeService.retrieveUpdateCkanPackage(organisationScheme, actualPackage.getId());
        } else {
            ckanPackage = organisationSchemeService.retrieveNewCkanPackage(organisationScheme);
        }

        if (ckanPackage != null) {
            Boolean resultado = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, actualPackage.getId());
            if (Boolean.TRUE.equals(resultado)) {
                if (actualPackage.getId() != null) {
                    List<CkanResource> resources = organisationSchemeService.retrieveUpdatedResources(organisationScheme, actualPackage, packageName);
                    ckanSenderService.updateResources(resources, urn);
                }
                return resultado;
            }
        }
        return false;
    }
}
