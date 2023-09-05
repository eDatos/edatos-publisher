package es.gobcan.istac.edatos.publisher.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.edatos.publisher.core.domain.CkanPackageRelationshipEntity;

@Repository
public interface CkanPackageRelationshipRepository extends JpaRepository<CkanPackageRelationshipEntity, String> {

    CkanPackageRelationshipEntity findByUrn(String urn);
}
