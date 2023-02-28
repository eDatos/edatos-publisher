package es.gobcan.istac.edatos.publisher.ckan.service.impl;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;

public interface CkanSenderService {

    Boolean createOrUpdateCkanPackage(CkanPackage ckanPackage, String urn, String packageId);
    CkanPackage getCkanPackageByName(String urn, String packageName);
}
