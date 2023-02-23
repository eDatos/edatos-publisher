package es.gobcan.istac.edatos.publisher.ckan.service.dataset.impl;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;

public interface DatasetSenderService {

    void createOrUpdateCkanPackage(CkanPackage ckanPackage, String urn);
}
