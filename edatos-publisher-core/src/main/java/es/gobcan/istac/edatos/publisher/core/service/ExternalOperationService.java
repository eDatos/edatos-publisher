package es.gobcan.istac.edatos.publisher.core.service;

import java.util.List;
import java.util.Optional;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalDatasetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalOperationEntity;

public interface ExternalOperationService {

    List<ExternalOperationEntity> findAll();
    Page<ExternalOperationEntity> find(String query, Pageable pageable);
    ExternalOperationEntity create(ExternalOperationEntity operation);
    ExternalOperationEntity update(ExternalOperationEntity operation);
    void delete(String urn);
    void delete(ExternalOperationEntity operation);
    List<ExternalOperationEntity> findByExternalCategoryUrnIn(List<String> urns);

    Optional<ExternalOperationEntity> findByUrn(String urn);

    List<ExternalOperationEntity> findByExternalOperationDatasetUrnIn(List<ExternalDatasetEntity> listDataset);

}
