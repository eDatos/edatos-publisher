package es.gobcan.istac.edatos.publisher.core.service.impl;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.domain.UsuarioEntity;
import es.gobcan.istac.edatos.publisher.core.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.edatos.publisher.core.errors.ErrorConstants;
import es.gobcan.istac.edatos.publisher.core.security.SecurityUtils;
import es.gobcan.istac.edatos.publisher.core.service.UsuarioService;
import es.gobcan.istac.edatos.publisher.core.util.QueryUtil;
import es.gobcan.istac.edatos.publisher.core.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private QueryUtil queryUtil;

    @Override
    public UsuarioEntity create(UsuarioEntity user) {
        return usuarioRepository.saveAndFlush(user);
    }

    @Override
    public void update(String firstName, String apellido1, String apellido2, String email) {
        usuarioRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setNombre(firstName);
            user.setApellido1(apellido1);
            user.setApellido2(apellido2);
            user.setEmail(email);
        });
    }

    @Override
    public UsuarioEntity update(UsuarioEntity user) {
        return usuarioRepository.saveAndFlush(user);
    }

    @Override
    public UsuarioEntity delete(String login) {
        UsuarioEntity usuario = usuarioRepository.findOneByLoginAndDeletionDateIsNull(login)
                .orElseThrow(() -> new CustomParameterizedExceptionBuilder().message("Usuario no válido").code(ErrorConstants.USUARIO_NO_VALIDO).build());
        usuario.setDeletionDate(Instant.now());
        usuario.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return usuarioRepository.saveAndFlush(usuario);
    }

    @Override
    public UsuarioEntity recover(String login) {
        UsuarioEntity usuario = usuarioRepository.findOneByLoginAndDeletionDateIsNotNull(login)
                .orElseThrow(() -> new CustomParameterizedExceptionBuilder().message("Usuario no válido").code(ErrorConstants.ENTIDAD_NO_ENCONTRADA, login).build());
        usuario.setDeletionDate(null);
        usuario.setDeletedBy(null);
        return usuarioRepository.saveAndFlush(usuario);
    }

    @Override
    public Page<UsuarioEntity> find(Pageable pageable, Boolean includeDeleted, String query) {
        DetachedCriteria criteria = buildUsuarioCriteria(pageable, includeDeleted, query);
        return usuarioRepository.findAll(criteria, pageable);
    }

    private DetachedCriteria buildUsuarioCriteria(Pageable pageable, Boolean includeDeleted, String query) {
        StringBuilder queryBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(query)) {
            queryBuilder.append(query);
        }
        String finalQuery = getFinalQuery(includeDeleted, queryBuilder);
        return queryUtil.queryToUserExternalUserCriteria(pageable, finalQuery);
    }

    private String getFinalQuery(Boolean includeDeleted, StringBuilder queryBuilder) {
        String finalQuery = queryBuilder.toString();
        if (BooleanUtils.isTrue(includeDeleted)) {
            finalQuery = queryUtil.queryIncludingDeleted(finalQuery);
        }
        return finalQuery;
    }

    @Override
    public Optional<UsuarioEntity> getUsuarioWithAuthoritiesByLogin(String login, Boolean includeDeleted) {
        if (BooleanUtils.isTrue(includeDeleted)) {
            return usuarioRepository.findOneByLogin(login);
        } else {
            return usuarioRepository.findOneWithRolesByLoginAndDeletionDateIsNull(login);
        }
    }

    @Override
    public UsuarioEntity getUsuarioWithAuthorities(Long id) {
        return usuarioRepository.findOneWithRolesByIdAndDeletionDateIsNull(id);
    }

    @Override
    public UsuarioEntity getUsuarioWithAuthorities() {
        UsuarioEntity returnValue = usuarioRepository.findOneWithRolesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(new UsuarioEntity());
        if (returnValue.getDeletionDate() != null) {
            returnValue.setRoles(new HashSet<>());
        }

        return returnValue;
    }
}
