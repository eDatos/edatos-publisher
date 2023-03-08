package es.gobcan.istac.edatos.publisher.core.organisation.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;

public interface OrganisationSchemeService {

    CkanPackage retrieveNewCkanPackage(OrganisationSchemeAvro organisationScheme);
    CkanPackage retrieveUpdateCkanPackage(OrganisationSchemeAvro organisationScheme, String id);
    String retrieveCkanPackageName(OrganisationSchemeAvro organisationScheme);
    List<CkanResource> retrieveUpdatedResources(OrganisationSchemeAvro organisationScheme, CkanPackage existingPackage, String packageId);
    List<CkanResource> retrieveNewResources(OrganisationSchemeAvro organisationScheme, String packageId);
}
