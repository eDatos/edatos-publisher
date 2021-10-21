package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalOperationEntity;
import es.gobcan.istac.edatos.publisher.core.domain.ExternalUserEntity;
import es.gobcan.istac.edatos.publisher.core.domain.FilterEntity;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalOperationRepository;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalUserRepository;
import es.gobcan.istac.edatos.publisher.rest.common.dto.FilterDto;
import es.gobcan.istac.edatos.publisher.rest.common.dto.FilterWithOperationCodeDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.config.AuditingMapperConfig;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.resolver.GenericMapperResolver;

@Mapper(componentModel = "spring", config = AuditingMapperConfig.class, uses = {GenericMapperResolver.class, ExternalUserMapper.class})
public abstract class FilterMapper implements EntityMapper<FilterDto, FilterEntity> {

    @Autowired
    private ExternalUserRepository externalUserRepository;

    @Autowired
    private ExternalOperationRepository externalOperationRepository;

    @Override
    @Mapping(target = "resourceName", ignore = true)
    public abstract FilterDto toDto(FilterEntity entity);

    /**
     * <code>defaultExpression</code> assigns a value in case the source is null. In
     * this case, mapstruct will generate the next piece of code:
     *
     * <pre>
     * if ( dto.getName() != null ) {
     *     filterEntity.setName( dto.getName() );
     * }
     * else {
     *     filterEntity.setName( dto.getResourceName() );
     * }
     * </pre>
     */
    @Override
    @Mapping(target = "name", defaultExpression = "java( dto.getResourceName() )")
    @Mapping(target = "lastAccessDate", defaultExpression = "java( java.time.Instant.now() )")
    @Mapping(target = "externalUser", source = "externalUser.id", qualifiedByName = "externalUserFromId")
    @Mapping(target = "externalOperation", ignore = true)
    public abstract FilterEntity toEntity(FilterDto dto);

    @Mapping(target = "name", defaultExpression = "java( dto.getResourceName() )")
    @Mapping(target = "lastAccessDate", defaultExpression = "java( java.time.Instant.now() )")
    @Mapping(target = "externalUser", source = "externalUser.id", qualifiedByName = "externalUserFromId")
    @Mapping(target = "externalOperation", source = "externalOperationCode", qualifiedByName = "externalOperationFromCode")
    public abstract FilterEntity toEntity(FilterWithOperationCodeDto dto);

    @Named("externalUserFromId")
    public ExternalUserEntity externalUserFromId(Long id) {
        if (id == null) {
            return null;
        }
        return externalUserRepository.findOne(id);
    }

    @Named("externalOperationFromCode")
    public ExternalOperationEntity externalOperationFromCode(String code) {
        if (code == null) {
            return null;
        }
        return externalOperationRepository.findByCode(code).orElse(null);
    }
}
