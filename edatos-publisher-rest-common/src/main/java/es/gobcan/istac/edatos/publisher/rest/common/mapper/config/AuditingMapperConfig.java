package es.gobcan.istac.edatos.publisher.rest.common.mapper.config;

import java.io.Serializable;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;

import es.gobcan.istac.edatos.publisher.core.domain.interfaces.AbstractAuditingEntity;

@MapperConfig(mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG)
public interface AuditingMapperConfig {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    AbstractAuditingEntity toEntity(Serializable dto);
}
