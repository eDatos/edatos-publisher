package es.gobcan.istac.edatos.publisher.core.service;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalCkanItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExternalCkanItemService {

    ExternalCkanItemEntity save(ExternalCkanItemEntity externalCkanItem);

    Page<ExternalCkanItemEntity> find(Pageable pageable, String query);
}
