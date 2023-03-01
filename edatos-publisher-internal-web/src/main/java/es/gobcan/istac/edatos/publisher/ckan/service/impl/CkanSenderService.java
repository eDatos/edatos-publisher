package es.gobcan.istac.edatos.publisher.ckan.service.impl;

import java.util.List;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;

public interface CkanSenderService {

    Boolean createOrUpdateCkanPackage(CkanPackage ckanPackage, String urn, String packageId);
    CkanPackage getCkanPackageByName(String urn, String packageName);
    Boolean updateResources(List<CkanResource> resources, String urn);
    Boolean createResources(List<CkanResource> resources, String urn);
}
