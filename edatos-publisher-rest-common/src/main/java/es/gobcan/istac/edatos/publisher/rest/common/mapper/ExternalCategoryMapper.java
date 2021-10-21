package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalCategoryEntity;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalCategoryRepository;
import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalCategoryDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.config.AuditingMapperConfig;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.resolver.GenericMapperResolver;

@Mapper(componentModel = "spring", config = AuditingMapperConfig.class, uses = {GenericMapperResolver.class, InternationalStringVOMapper.class})
public abstract class ExternalCategoryMapper implements EntityMapper<ExternalCategoryDto, ExternalCategoryEntity> {

    @Autowired
    ExternalCategoryRepository externalCategoryRepository;

    @Override
    public abstract ExternalCategoryDto toDto(ExternalCategoryEntity entity);

    @Override
    public abstract ExternalCategoryEntity toEntity(ExternalCategoryDto dto);

    public ExternalCategoryDto getFromUrn(String urn) {
        return toDto(externalCategoryRepository.getByUrn(urn).orElse(null));
    }
}
