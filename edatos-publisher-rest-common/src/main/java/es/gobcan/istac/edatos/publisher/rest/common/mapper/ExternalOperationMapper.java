package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import java.time.Instant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.siemac.metamac.statistical.operations.core.stream.messages.DatetimeAvro;
import org.siemac.metamac.statistical.operations.core.stream.messages.InternationalStringAvro;
import org.siemac.metamac.statistical.operations.core.stream.messages.OperationAvro;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalOperationEntity;
import es.gobcan.istac.edatos.publisher.core.domain.vo.InternationalStringVO;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalOperationRepository;
import es.gobcan.istac.edatos.publisher.core.service.FavoriteService;
import es.gobcan.istac.edatos.publisher.core.service.FilterService;
import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalOperationDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.config.AuditingMapperConfig;

@Mapper(componentModel = "spring", config = AuditingMapperConfig.class, uses = {InternationalStringVOMapper.class, ExternalCategoryMapper.class})
public abstract class ExternalOperationMapper implements EntityMapper<ExternalOperationDto, ExternalOperationEntity> {

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    FilterService filterService;

    @Autowired
    ExternalOperationRepository externalOperationRepository;

    @Override
    @Mapping(target = "category", source = "entity.externalCategoryUrn")
    @Mapping(target = "subscribers", expression = "java(favoriteService.getOperationSubscribers().getOrDefault(entity.getId(), 0L))")
    @Mapping(target = "numberOfFilters", expression = "java(filterService.getOperationFilters().getOrDefault(entity.getUrn(), 0L))")
    public abstract ExternalOperationDto toDto(ExternalOperationEntity entity);

    @Override
    @Mapping(target = "externalCategoryUrn", source = "dto.category.urn")
    public abstract ExternalOperationEntity toEntity(ExternalOperationDto dto);

    public ExternalOperationEntity toEntity(long id, boolean enabled, boolean notificationsEnabled) {
        ExternalOperationEntity entity = externalOperationRepository.findOne(id);
        entity.setEnabled(enabled);
        entity.setNotificationsEnabled(notificationsEnabled);
        return entity;
    }

    @Mapping(target = "name", source = "title")
    @Mapping(target = "externalCategoryUrn", source = "subjectArea.urn")
    @Mapping(target = "publicationDate", source = "inventoryDate")
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "optLock", ignore = true)
    @Mapping(target = "notificationsEnabled", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    public abstract ExternalOperationEntity toEntity(OperationAvro source);

    @Mapping(target = "texts", source = "localisedStrings")
    public abstract InternationalStringVO toVO(InternationalStringAvro source);

    public Instant toInstant(DatetimeAvro datetime) {
        if (datetime == null) {
            return null;
        }
        return Instant.ofEpochMilli(datetime.getInstant());
    }

    @ObjectFactory
    public ExternalOperationEntity resolver(OperationAvro operation, @TargetType Class<ExternalOperationEntity> type) {
        if (operation == null || operation.getUrn() == null) {
            return new ExternalOperationEntity();
        }
        return externalOperationRepository.findByUrn(operation.getUrn()).orElseGet(ExternalOperationEntity::new);
    }
}
