package es.gobcan.istac.edatos.publisher.core.repository;

import java.util.Optional;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.edatos.publisher.core.domain.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findOneByEmail(String email);

    Optional<UsuarioEntity> findOneByLogin(String login);

    @EntityGraph(attributePaths = "roles")
    Optional<UsuarioEntity> findOneWithRolesByLogin(String login);

    @EntityGraph(attributePaths = "roles")
    Optional<UsuarioEntity> findOneWithRolesByLoginAndDeletionDateIsNull(String login);

    @EntityGraph(attributePaths = "roles")
    UsuarioEntity findOneWithRolesByIdAndDeletionDateIsNull(Long id);

    Optional<UsuarioEntity> findOneByLoginAndDeletionDateIsNull(String login);

    Page<UsuarioEntity> findAll(DetachedCriteria criteria, Pageable pageable);

    Optional<UsuarioEntity> findOneByLoginAndDeletionDateIsNotNull(String login);
}
