package es.gobcan.istac.edatos.publisher.core.repository;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.edatos.publisher.core.domain.CategoryEntity;
import es.gobcan.istac.edatos.publisher.core.domain.ExternalCategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> deleteById(Long id);
    Page<CategoryEntity> findAll(DetachedCriteria criteria, Pageable pageable);
    List<CategoryEntity> getByParentIsNull();

    @Query(value = "select new org.apache.commons.lang3.tuple.ImmutablePair(cat, extCat) from CategoryEntity cat join cat.externalCategories extCat")
    List<ImmutablePair<CategoryEntity, ExternalCategoryEntity>> getCategoryExternalCategories();
}
