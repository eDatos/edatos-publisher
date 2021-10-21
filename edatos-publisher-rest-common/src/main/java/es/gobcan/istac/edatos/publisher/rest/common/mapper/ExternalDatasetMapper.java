package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringAvro;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalDatasetEntity;
import es.gobcan.istac.edatos.publisher.core.domain.vo.InternationalStringVO;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalDatasetRepository;
import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalDatasetDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.config.AuditingMapperConfig;

@Mapper(componentModel = "spring",
        config = AuditingMapperConfig.class,
        uses = {InternationalStringVOMapper.class, ExternalOperationMapper.class})
public abstract class ExternalDatasetMapper implements EntityMapper<ExternalDatasetDto, ExternalDatasetEntity> {

    @Autowired
    ExternalDatasetRepository externalDatasetRepository;

    @Mapping(target = "name",
             source = "siemacMetadataStatisticalResource.lifecycleStatisticalResource.versionableStatisticalResource.nameableStatisticalResource.title")
    @Mapping(target = "urn",
             source = "siemacMetadataStatisticalResource.lifecycleStatisticalResource.versionableStatisticalResource.nameableStatisticalResource.identifiableStatisticalResource.urn")
    @Mapping(target = "code",
             source = "siemacMetadataStatisticalResource.lifecycleStatisticalResource.versionableStatisticalResource.nameableStatisticalResource.identifiableStatisticalResource.code")
    @Mapping(target = "externalOperationUrn",
             source = "siemacMetadataStatisticalResource.lifecycleStatisticalResource.versionableStatisticalResource.nameableStatisticalResource.identifiableStatisticalResource.statisticalOperation.urn")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "optLock", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    public abstract ExternalDatasetEntity toEntity(DatasetVersionAvro value);

    @Mapping(target = "texts", source = "localisedStrings")
    public abstract InternationalStringVO toVO(InternationalStringAvro source);

    @ObjectFactory
    public ExternalDatasetEntity resolver(DatasetVersionAvro dataset, @TargetType Class<ExternalDatasetEntity> type) {
        if (dataset == null) {
            return new ExternalDatasetEntity();
        }
        String urn = dataset.getSiemacMetadataStatisticalResource()
                            .getLifecycleStatisticalResource()
                            .getVersionableStatisticalResource()
                            .getNameableStatisticalResource()
                            .getIdentifiableStatisticalResource()
                            .getUrn();
        if (urn == null) {
            return new ExternalDatasetEntity();
        }
        return externalDatasetRepository.findByUrn(urn).orElseGet(ExternalDatasetEntity::new);
    }
}
