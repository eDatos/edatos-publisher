package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import es.gobcan.istac.edatos.publisher.rest.common.dto.UsuarioDto;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.publisher.core.domain.UsuarioEntity;
import es.gobcan.istac.edatos.publisher.core.repository.UsuarioRepository;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.config.AuditingMapperConfig;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.resolver.GenericMapperResolver;

@Mapper(componentModel = "spring", config = AuditingMapperConfig.class, uses = {GenericMapperResolver.class})
public abstract class UsuarioMapper implements EntityMapper<UsuarioDto, UsuarioEntity> {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioEntity getUserFromLogin(String login) {
        return usuarioRepository.findOneByLogin(login).orElse(null);
    }
}
