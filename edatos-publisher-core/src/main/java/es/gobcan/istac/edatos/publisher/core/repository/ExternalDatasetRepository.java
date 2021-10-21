package es.gobcan.istac.edatos.publisher.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalDatasetEntity;

@Repository
public interface ExternalDatasetRepository extends AbstractExternalItemRepository<ExternalDatasetEntity> {

    List<ExternalDatasetEntity> findByExternalOperationUrn(String urn);
    List<ExternalDatasetEntity> findByExternalOperationUrnIn(List<String> urns);
    Optional<ExternalDatasetEntity> findByCode(String code);
    Optional<ExternalDatasetEntity> findByUrn(String urn);

    @Override
    List<ExternalDatasetEntity> findAll();

}
