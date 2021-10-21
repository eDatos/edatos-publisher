package es.gobcan.istac.edatos.publisher.core.service;

import java.util.Optional;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalDatasetEntity;

import java.util.List;

public interface ExternalDatasetService {

    ExternalDatasetEntity create(ExternalDatasetEntity dataset);
    ExternalDatasetEntity update(ExternalDatasetEntity dataset);
    void delete(String urn);
    void delete(ExternalDatasetEntity dataset);
    Optional<ExternalDatasetEntity> findByUrn(String urn);

    List<ExternalDatasetEntity> getLastDayDatasets();

}
