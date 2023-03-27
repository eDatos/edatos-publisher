package es.gobcan.istac.edatos.publisher.core.concept.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;

public interface ConceptSchemePublisherService {

    CkanPackage retrieveNewCkanPackage(ConceptSchemeAvro conceptScheme);
    CkanPackage retrieveUpdateCkanPackage(ConceptSchemeAvro conceptScheme, String id);
    String retrieveCkanPackageName(ConceptSchemeAvro conceptScheme);
    List<CkanResource> retrieveUpdatedResources(ConceptSchemeAvro conceptScheme, CkanPackage existingPackage, String packageId);
    List<CkanResource> retrieveNewResources(ConceptSchemeAvro conceptScheme, CkanPackage existingPackage);

}
