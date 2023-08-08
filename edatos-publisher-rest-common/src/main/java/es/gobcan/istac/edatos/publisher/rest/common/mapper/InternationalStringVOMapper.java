package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.srm.core.stream.message.InternationalStringAvro;
import org.siemac.metamac.srm.core.stream.message.LocalisedStringAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;

import es.gobcan.istac.edatos.publisher.core.domain.vo.InternationalStringVO;
import es.gobcan.istac.edatos.publisher.core.domain.vo.LocalisedStringVO;
import es.gobcan.istac.edatos.publisher.rest.common.dto.InternationalStringDto;

@Mapper(componentModel = "spring")
public interface InternationalStringVOMapper extends EntityMapper<InternationalStringDto, InternationalStringVO> {

    @Override
    InternationalStringDto toDto(InternationalStringVO entity);

    @Override
    InternationalStringVO toEntity(InternationalStringDto dto);

    public abstract InternationalStringVO toInternationalStringVO(InternationalString internationalStringVO);

    @Mapping(target = "locale", source = "lang")
    @Mapping(target = "label", source = "value")
    public abstract LocalisedStringVO toLocalisedStringVO(LocalisedString internationalStringVO);

    public abstract LocalisedStringVO toLocalisedStringVO(LocalisedStringAvro localisedStringAvro);

    public abstract Set<LocalisedStringVO> toSrmLocalisedStringVOList(List<LocalisedStringAvro> localisedStringAvroList);

    @Mapping(target = "texts", source = "localisedStrings")
    public abstract InternationalStringVO toInternationalStringVO(InternationalStringAvro internationalStringAvro);

    public abstract LocalisedStringVO toLocalisedStringVO(InternationalStringItemAvro internationalStringItemAvro);

    public abstract Set<LocalisedStringVO> toStatisticalResourcesLocalisedStringVOList(List<InternationalStringItemAvro> internationalStringItemAvroList);

    @Mapping(target = "texts", source = "localisedStrings")
    public abstract InternationalStringVO toInternationalStringVO(org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringAvro internationalStringAvro);

}
