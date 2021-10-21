package es.gobcan.istac.edatos.publisher.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalCategoryEntity;

public interface ExternalCategoryService {

    List<ExternalCategoryEntity> requestAllExternalCategories();
    Page<ExternalCategoryEntity> requestExternalCategories(Pageable pageable, String search);
    List<ExternalCategoryEntity> findAll();
}
