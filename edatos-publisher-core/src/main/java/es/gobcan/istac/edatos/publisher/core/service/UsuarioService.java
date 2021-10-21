package es.gobcan.istac.edatos.publisher.core.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.gobcan.istac.edatos.publisher.core.domain.UsuarioEntity;

public interface UsuarioService {

    UsuarioEntity create(UsuarioEntity user);

    void update(String firstName, String apellido1, String apellido2, String email);

    UsuarioEntity update(UsuarioEntity user);

    UsuarioEntity delete(String login);

    UsuarioEntity recover(String login);

    Page<UsuarioEntity> find(Pageable pageable, Boolean includeDeleted, String query);

    Optional<UsuarioEntity> getUsuarioWithAuthoritiesByLogin(String login, Boolean includeDeleted);

    UsuarioEntity getUsuarioWithAuthorities(Long id);

    UsuarioEntity getUsuarioWithAuthorities();
}
