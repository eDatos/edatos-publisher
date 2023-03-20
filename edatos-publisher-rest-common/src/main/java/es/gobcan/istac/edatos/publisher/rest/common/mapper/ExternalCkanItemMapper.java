package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import org.mapstruct.Mapper;
import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;
import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
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

    public ExternalCkanItemEntity toExternalCkanItemEntity(CodelistAvro codelist, CkanState state) {
        ExternalCkanItemEntity externalCkanItem = new ExternalCkanItemEntity();
        String[] params = UrnUtils.splitUrnItem(codelist.getUrn());
        externalCkanItem.setCkanState(state);
        externalCkanItem.setKafkaMessage("{}");
        externalCkanItem.setCode(params[1]);
        externalCkanItem.setName(internationalStringMapper.toInternationalStringVO(codelist.getName()));
        externalCkanItem.setType(TypeExternalArtefactsEnum.CODELIST);
        externalCkanItem.setUrn(codelist.getUrn());
        return externalCkanItem;
    }

    public ExternalCkanItemEntity toExternalCkanItemEntity(ConceptSchemeAvro conceptScheme, CkanState state) {
        ExternalCkanItemEntity externalCkanItem = new ExternalCkanItemEntity();
        String[] params = UrnUtils.splitUrnItem(conceptScheme.getUrn());
        externalCkanItem.setCkanState(state);
        externalCkanItem.setKafkaMessage("{}");
        externalCkanItem.setCode(params[1]);
        externalCkanItem.setName(internationalStringMapper.toInternationalStringVO(conceptScheme.getName()));
        externalCkanItem.setType(TypeExternalArtefactsEnum.CONCEPT_SCHEME);
        externalCkanItem.setUrn(conceptScheme.getUrn());
        return externalCkanItem;
    }

    public ExternalCkanItemEntity toExternalCkanItemEntity(CategorySchemeAvro categoryScheme, CkanState state) {
        ExternalCkanItemEntity externalCkanItem = new ExternalCkanItemEntity();
        String[] params = UrnUtils.splitUrnItem(categoryScheme.getUrn());
        externalCkanItem.setCkanState(state);
        externalCkanItem.setKafkaMessage("{}");
        externalCkanItem.setCode(params[1]);
        externalCkanItem.setName(internationalStringMapper.toInternationalStringVO(categoryScheme.getName()));
        externalCkanItem.setType(TypeExternalArtefactsEnum.CATEGORY_SCHEME);
        externalCkanItem.setUrn(categoryScheme.getUrn());
        return externalCkanItem;
    }

    public ExternalCkanItemEntity toExternalCkanItemEntity(OrganisationSchemeAvro organisationScheme, CkanState state) {
        ExternalCkanItemEntity externalCkanItem = new ExternalCkanItemEntity();

        String[] params = UrnUtils.splitUrnItem(organisationScheme.getUrn());
        externalCkanItem.setCkanState(state);
        externalCkanItem.setKafkaMessage("{}");
        externalCkanItem.setCode(params[1]);
        externalCkanItem.setName(internationalStringMapper.toInternationalStringVO(organisationScheme.getName()));
        externalCkanItem.setType(TypeExternalArtefactsEnum.valueOf(organisationScheme.getOrganisationSchemeType().name()));
        externalCkanItem.setUrn(organisationScheme.getUrn());
        return externalCkanItem;
    }
}
