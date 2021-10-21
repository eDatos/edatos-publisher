package es.gobcan.istac.edatos.publisher.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalItemEntity;

/**
 * @see <a href="https://stackoverflow.com/a/27549198">Best way of handling entities inheritance in Spring Data JPA</a>
 * @param <T> A class that is equal to {@link ExternalItemEntity} or extends it.
 */
@NoRepositoryBean
public interface AbstractExternalItemRepository<T extends ExternalItemEntity> extends JpaRepository<T, Long> {

    Optional<T> getByUrn(String urn);
    void deleteByUrn(String urn);
}
