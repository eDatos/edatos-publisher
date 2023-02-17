package es.gobcan.istac.edatos.publisher.core.repository;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalCkanItemEntity;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalCkanItemRepository extends JpaRepository<ExternalCkanItemEntity, Long>  {

    Page<ExternalCkanItemEntity> findAll(DetachedCriteria criteria, Pageable pageable);
}
