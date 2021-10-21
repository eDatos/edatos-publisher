package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalUserEntity;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalUserRepository;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.config.AuditingMapperConfig;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.resolver.GenericMapperResolver;

@Mapper(componentModel = "spring", config = AuditingMapperConfig.class, uses= {GenericMapperResolver.class, ExternalUserRepository.class})
public abstract class ExternalUserMapper implements EntityMapper<ExternalUserDto, ExternalUserEntity> {
    @Override
    @Mapping(target = "password", ignore = true)
    public abstract ExternalUserDto toDto(ExternalUserEntity entity);

    @Override
    @Mapping(target = "password", ignore = true)
    public abstract ExternalUserEntity toEntity(ExternalUserDto dto);
}
