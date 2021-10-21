package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.publisher.core.domain.CategoryEntity;
import es.gobcan.istac.edatos.publisher.core.domain.ExternalOperationEntity;
import es.gobcan.istac.edatos.publisher.core.domain.ExternalUserEntity;
import es.gobcan.istac.edatos.publisher.core.domain.FavoriteEntity;
import es.gobcan.istac.edatos.publisher.core.repository.CategoryRepository;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalOperationRepository;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalUserRepository;
import es.gobcan.istac.edatos.publisher.rest.common.dto.FavoriteDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.config.AuditingMapperConfig;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.resolver.GenericMapperResolver;

@Mapper(componentModel = "spring", config = AuditingMapperConfig.class, uses = {GenericMapperResolver.class, ExternalUserAccountMapper.class, CategoryMapper.class})
public abstract class FavoriteMapper implements EntityMapper<FavoriteDto, FavoriteEntity> {

    @Autowired
    private ExternalUserRepository externalUserRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExternalOperationRepository externalOperationRepository;

    @Override
    @Mapping(target = "externalUser", source = "externalUser", qualifiedByName = "externalUserToBaseDto")
    public abstract FavoriteDto toDto(FavoriteEntity entity);

    @Override
    @Mapping(target = "externalUser", source = "externalUser.id", qualifiedByName = "externalUserFromId")
    @Mapping(target = "category", source = "category.id", qualifiedByName = "categoryFromId")
    @Mapping(target = "externalOperation", source = "externalOperation.id", qualifiedByName = "externalOperationFromId")
    public abstract FavoriteEntity toEntity(FavoriteDto dto);

    @Named("externalUserFromId")
    public ExternalUserEntity externalUserFromId(Long id) {
        if (id == null) {
            return null;
        }
        return externalUserRepository.findOne(id);
    }

    @Named("categoryFromId")
    public CategoryEntity categoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        return categoryRepository.findOne(id);
    }

    @Named("externalOperationFromId")
    public ExternalOperationEntity externalOperationFromId(Long id) {
        if (id == null) {
            return null;
        }
        return externalOperationRepository.findOne(id);
    }
}
