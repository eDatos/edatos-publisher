package es.gobcan.istac.edatos.publisher.core.dataset.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;

import es.gobcan.istac.edatos.publisher.core.domain.vo.InternationalStringVO;
import es.gobcan.istac.edatos.publisher.core.domain.vo.LocalisedStringVO;

@Mapper(componentModel = "spring")
public abstract class InternationalStringMapper {

    public abstract InternationalStringVO toInternationalStringVO(InternationalString internationalStringVO);

    @Mapping(target = "locale", source = "lang")
    @Mapping(target = "label", source = "value")
    public abstract LocalisedStringVO toLocalisedStringVO(LocalisedString internationalStringVO);
}
