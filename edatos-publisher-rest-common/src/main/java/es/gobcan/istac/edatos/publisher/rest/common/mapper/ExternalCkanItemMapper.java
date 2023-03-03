package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import org.mapstruct.Mapper;
import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalCkanItemEntity;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;
import es.gobcan.istac.edatos.publisher.core.mapper.config.AuditingMapperConfig;
import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalCkanItemDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.resolver.GenericMapperResolver;

@Mapper(componentModel = "spring", config = AuditingMapperConfig.class, uses = {GenericMapperResolver.class, InternationalStringVOMapper.class})
public abstract class ExternalCkanItemMapper implements EntityMapper<ExternalCkanItemDto, ExternalCkanItemEntity> {

    @Autowired
    private InternationalStringVOMapper internationalStringMapper;

    public ExternalCkanItemEntity toExternalCkanItemEntity(DatasetVersionAvro datasetVersion, CkanState state) {

        ExternalCkanItemEntity externalCkanItem = new ExternalCkanItemEntity();
        externalCkanItem.setCkanState(state);
        externalCkanItem.setKafkaMessage("{}");
        externalCkanItem.setCode(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getCode());
        externalCkanItem.setName(internationalStringMapper.toInternationalStringVO(
                datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource().getTitle()));
        externalCkanItem.setType(TypeExternalArtefactsEnum.DATASET);
        externalCkanItem.setUrn(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn());
        return externalCkanItem;
    }
    /*
     * public ExternalCkanItemEntity toExternalCkanItemEntity(IndicatorVersionAvro indicatorVersion) {
     * ExternalCkanItemEntity externalCkanItem = new ExternalCkanItemEntity();
     * externalCkanItem.setCkanState();
     * externalCkanItem.setKafkaMessage();
     * externalCkanItem.setCode(indicatorVersion.getCode());
     * externalCkanItem.setName(internationalStringMapper.toInternationalStringVO(indicatorVersion.getTitle()));
     * externalCkanItem.setType(TypeExternalArtefactsEnum.INDICATOR);
     * externalCkanItem.setUrn();
     * return externalCkanItem;
     * }
     * public ExternalCkanItemEntity toExternalCkanItemEntity(CodelistAvro codelist) {
     * ExternalCkanItemEntity externalCkanItem = new ExternalCkanItemEntity();
     * externalCkanItem.setCkanState();
     * externalCkanItem.setKafkaMessage();
     * externalCkanItem.setCode();
     * externalCkanItem.setName(internationalStringMapper.toInternationalStringVO(codelist.getName()));
     * externalCkanItem.setType(TypeExternalArtefactsEnum.CODELIST);
     * externalCkanItem.setUrn(codelist.getUrn());
     * return externalCkanItem;
     * }
     * public ExternalCkanItemEntity toExternalCkanItemEntity(ConceptSchemeAvro conceptScheme) {
     * ExternalCkanItemEntity externalCkanItem = new ExternalCkanItemEntity();
     * externalCkanItem.setCkanState();
     * externalCkanItem.setKafkaMessage();
     * externalCkanItem.setCode();
     * externalCkanItem.setName(internationalStringMapper.toInternationalStringVO(conceptScheme.getName()));
     * externalCkanItem.setType(TypeExternalArtefactsEnum.CONCEPT_SCHEME);
     * externalCkanItem.setUrn(conceptScheme.getUrn());
     * return externalCkanItem;
     * }
     * public ExternalCkanItemEntity toExternalCkanItemEntity(OrganisationSchemeAvro organisationScheme) {
     * ExternalCkanItemEntity externalCkanItem = new ExternalCkanItemEntity();
     * externalCkanItem.setCkanState();
     * externalCkanItem.setKafkaMessage();
     * externalCkanItem.setCode();
     * externalCkanItem.setName(internationalStringMapper.toInternationalStringVO(organisationScheme.getName()));
     * externalCkanItem.setType(TypeExternalArtefactsEnum.ORGANISATION_SCHEME);
     * externalCkanItem.setUrn(organisationScheme.getUrn());
     * return externalCkanItem;
     * }
     */

    /*
     * public ExternalCkanItemEntity toExternalCkanItemEntity(CategorySchemeAvro categoryScheme) {
     * ExternalCkanItemEntity externalCkanItem = new ExternalCkanItemEntity();
     * externalCkanItem.setCkanState(CkanState.SENT);
     * externalCkanItem.setKafkaMessage("{}");
     * externalCkanItem.setCode("indeiticador");
     * externalCkanItem.setName(internationalStringMapper.toInternationalStringVO(categoryScheme.getName()));
     * externalCkanItem.setType(TypeExternalArtefactsEnum.CATEGORY_SCHEME);
     * externalCkanItem.setUrn(categoryScheme.getUrn());
     * return externalCkanItem;
     * }
     */
}
