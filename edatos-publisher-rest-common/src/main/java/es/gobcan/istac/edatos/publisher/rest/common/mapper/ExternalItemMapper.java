package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import org.mapstruct.Mapper;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalItemEntity;
import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalItemDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.config.AuditingMapperConfig;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.resolver.GenericMapperResolver;

@Mapper(componentModel = "spring", config = AuditingMapperConfig.class, uses = {GenericMapperResolver.class, InternationalStringVOMapper.class})
public abstract class ExternalItemMapper implements EntityMapper<ExternalItemDto, ExternalItemEntity> {
    @Override
    public abstract ExternalItemDto toDto(ExternalItemEntity entity);

    @Override
    public abstract ExternalItemEntity toEntity(ExternalItemDto dto);
}
