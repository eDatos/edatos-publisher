package es.gobcan.istac.edatos.publisher.core.service;

import es.gobcan.istac.edatos.publisher.core.domain.CkanPackageRelationshipEntity;

public interface CkanPackageRelationshipService {

    void delete(CkanPackageRelationshipEntity ckanPackageRelationshipEntity);
    CkanPackageRelationshipEntity getByUrn(String urn);
    CkanPackageRelationshipEntity updateOrCreate(CkanPackageRelationshipEntity ckanPackageRelationshipEntity);

}
